/*
 * Copyright (c) 2002-2026, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.elasticdata.modules.suggest.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jsoup.Jsoup;

import fr.paris.lutece.plugins.elasticdata.business.AbstractDataSource;
import fr.paris.lutece.plugins.elasticdata.business.DataObject;
import fr.paris.lutece.plugins.elasticdata.business.IndexerAction;
import fr.paris.lutece.plugins.elasticdata.service.DataSourceIncrementalService;
import fr.paris.lutece.plugins.suggest.business.SubmitFilter;
import fr.paris.lutece.plugins.suggest.business.Suggest;
import fr.paris.lutece.plugins.suggest.business.SuggestHome;
import fr.paris.lutece.plugins.suggest.business.SuggestSubmit;
import fr.paris.lutece.plugins.suggest.business.SuggestSubmitHome;
import fr.paris.lutece.plugins.suggest.business.SuggestUserInfo;
import fr.paris.lutece.plugins.suggest.business.SuggestUserInfoHome;
import fr.paris.lutece.plugins.suggest.service.SuggestPlugin;
import fr.paris.lutece.plugins.suggest.service.workflow.SuggestWorkflowService;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Data source of the suggest submits, whatever their publication state.
 */
@ApplicationScoped
public class SuggestDataSource extends AbstractDataSource
{
    private static final String DOCUMENT_TYPE_NAME = "suggestSubmit";
    private static final String AUTHOR_KEY = "key";
    private static final String AUTHOR_LOGIN = "login";
    private static final String AUTHOR_FIRST_NAME = "firstName";
    private static final String AUTHOR_LAST_NAME = "lastName";
    private static final String AUTHOR_BUSINESS_MAIL = "businessMail";
    private static final String AUTHOR_HOME_MAIL = "homeMail";
    private static final String AUTHOR_DEPARTMENT = "department";

    /**
     * Constructor used by the CDI proxy.
     */
    public SuggestDataSource( )
    {
    }

    /**
     * Constructor.
     *
     * @param strId
     *            the data source id
     * @param strName
     *            the data source name
     * @param strTargetIndexName
     *            the target index name
     * @param strMappings
     *            the index mappings
     */
    @Inject
    public SuggestDataSource( @ConfigProperty( name = "elasticdata-suggest.suggestDataSource.id" ) String strId,
            @ConfigProperty( name = "elasticdata-suggest.suggestDataSource.name" ) String strName,
            @ConfigProperty( name = "elasticdata-suggest.suggestDataSource.targetIndexName" ) String strTargetIndexName,
            @ConfigProperty( name = "elasticdata-suggest.suggestDataSource.mappings" ) String strMappings )
    {
        setId( strId );
        setName( strName );
        setTargetIndexName( strTargetIndexName );
        setMappings( strMappings );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getIdDataObjects( )
    {
        return SuggestSubmitHome.getSuggestSubmitListId( new SubmitFilter( ), getSuggestPlugin( ) ).stream( ).map( String::valueOf ).toList( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DataObject> getDataObjects( List<String> listIdDataObjects )
    {
        Plugin plugin = getSuggestPlugin( );
        Map<Integer, Suggest> mapSuggests = new HashMap<>( );
        List<DataObject> listDataObjects = new ArrayList<>( );

        for ( String strId : listIdDataObjects )
        {
            SuggestSubmit submit = SuggestSubmitHome.findByPrimaryKey( Integer.parseInt( strId ), plugin );

            if ( submit != null )
            {
                Suggest suggest = mapSuggests.computeIfAbsent( submit.getSuggest( ).getIdSuggest( ), nId -> SuggestHome.findByPrimaryKey( nId, plugin ) );
                listDataObjects.add( buildDataObject( submit, suggest, plugin ) );
            }
        }

        return listDataObjects;
    }

    /**
     * Queues the indexing of a suggest submit.
     *
     * @param nIdSuggestSubmit
     *            the suggest submit id
     * @param nIdTask
     *            the indexer task
     */
    public void indexDocument( int nIdSuggestSubmit, int nIdTask )
    {
        DataSourceIncrementalService.addTask( getId( ), String.valueOf( nIdSuggestSubmit ), nIdTask );
    }

    /**
     * Queues the deletion of a suggest submit. The plugin cancels a pending indexing instead of queueing the deletion, so a second call queues it
     * when the first one only cancelled.
     *
     * @param nIdSuggestSubmit
     *            the suggest submit id
     */
    public void deleteDocument( int nIdSuggestSubmit )
    {
        indexDocument( nIdSuggestSubmit, IndexerAction.TASK_DELETE );
        indexDocument( nIdSuggestSubmit, IndexerAction.TASK_DELETE );
    }

    /**
     * Builds the document of a suggest submit.
     *
     * @param submit
     *            the suggest submit
     * @param suggest
     *            the suggest owning the submit
     * @param plugin
     *            the suggest plugin
     * @return the document
     */
    private SuggestSubmitDataObject buildDataObject( SuggestSubmit submit, Suggest suggest, Plugin plugin )
    {
        SuggestSubmitDataObject dataObject = new SuggestSubmitDataObject( );
        dataObject.setId( String.valueOf( submit.getIdSuggestSubmit( ) ) );
        dataObject.setDocumentTypeName( DOCUMENT_TYPE_NAME );
        dataObject.setTimestamp( submit.getDateResponse( ).getTime( ) );
        dataObject.setIdSuggestSubmit( submit.getIdSuggestSubmit( ) );
        dataObject.setTitle( submit.getSuggestSubmitTitle( ) );

        if ( submit.getSuggestSubmitValue( ) != null )
        {
            dataObject.setContent( Jsoup.parse( submit.getSuggestSubmitValue( ) ).text( ) );
        }

        if ( submit.getSuggestSubmitState( ) != null )
        {
            dataObject.setState( submit.getSuggestSubmitState( ).getTitle( ) );
        }

        if ( submit.getSuggestSubmitType( ) != null )
        {
            dataObject.setType( submit.getSuggestSubmitType( ).getName( ) );
        }

        if ( submit.getCategory( ) != null )
        {
            dataObject.setCategory( submit.getCategory( ).getTitle( ) );
        }

        dataObject.setVoteCount( submit.getNumberVote( ) );
        dataObject.setScore( submit.getNumberScore( ) );
        dataObject.setCommentCount( submit.getNumberCommentEnable( ) );
        dataObject.setViewCount( submit.getNumberView( ) );
        dataObject.setPinned( submit.isPinned( ) );
        dataObject.setReported( submit.isReported( ) );

        if ( suggest != null )
        {
            dataObject.setParentId( String.valueOf( suggest.getIdSuggest( ) ) );
            dataObject.setParentName( suggest.getTitle( ) );

            State state = SuggestWorkflowService.getState( suggest, submit );

            if ( state != null )
            {
                dataObject.setWorkflowState( state.getName( ) );
            }
        }

        if ( submit.getLuteceUserKey( ) != null )
        {
            dataObject.setAuthor( getAuthor( submit.getLuteceUserKey( ), plugin ) );
        }

        return dataObject;
    }

    /**
     * Returns the stored info of an author.
     *
     * @param strLuteceUserKey
     *            the author key
     * @param plugin
     *            the suggest plugin
     * @return the author info, with the key only when no info is stored
     */
    private Map<String, String> getAuthor( String strLuteceUserKey, Plugin plugin )
    {
        Map<String, String> mapAuthor = new LinkedHashMap<>( );
        mapAuthor.put( AUTHOR_KEY, strLuteceUserKey );

        SuggestUserInfo userInfo = SuggestUserInfoHome.findByKey( strLuteceUserKey, plugin );

        if ( userInfo != null )
        {
            mapAuthor.put( AUTHOR_LOGIN, userInfo.getLogin( ) );
            mapAuthor.put( AUTHOR_FIRST_NAME, userInfo.getFirstName( ) );
            mapAuthor.put( AUTHOR_LAST_NAME, userInfo.getLastName( ) );
            mapAuthor.put( AUTHOR_BUSINESS_MAIL, userInfo.getBusinessMail( ) );
            mapAuthor.put( AUTHOR_HOME_MAIL, userInfo.getHomeMail( ) );
            mapAuthor.put( AUTHOR_DEPARTMENT, userInfo.getDepartment( ) );
        }

        return mapAuthor;
    }

    /**
     * Returns the suggest plugin.
     *
     * @return the suggest plugin
     */
    private static Plugin getSuggestPlugin( )
    {
        return PluginService.getPlugin( SuggestPlugin.PLUGIN_NAME );
    }
}
