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
package fr.paris.lutece.plugins.elasticdata.modules.suggest.service.listener;

import org.apache.commons.lang3.math.NumberUtils;

import fr.paris.lutece.plugins.elasticdata.business.IndexerAction;
import fr.paris.lutece.plugins.elasticdata.modules.suggest.business.SuggestDataSource;
import fr.paris.lutece.plugins.suggest.business.SuggestSubmit;
import fr.paris.lutece.plugins.suggest.service.event.SuggestSubmitEvent;
import fr.paris.lutece.portal.business.event.ResourceEvent;
import fr.paris.lutece.portal.service.event.EventAction;
import fr.paris.lutece.portal.service.event.Type;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.inject.Inject;

/**
 * Queues the indexing of a suggest submit when suggest creates, updates or removes it, and when its workflow state changes.
 */
@ApplicationScoped
public class SuggestSubmitIndexerEventListener
{
    @Inject
    private SuggestDataSource _suggestDataSource;

    /**
     * Queues the indexing of a created suggest submit.
     *
     * @param event
     *            the suggest submit event
     */
    public void onCreated( @ObservesAsync @Type( EventAction.CREATE ) SuggestSubmitEvent event )
    {
        _suggestDataSource.indexDocument( event.getIdSuggestSubmit( ), IndexerAction.TASK_CREATE );
    }

    /**
     * Queues the reindexing of an updated suggest submit.
     *
     * @param event
     *            the suggest submit event
     */
    public void onUpdated( @ObservesAsync @Type( EventAction.UPDATE ) SuggestSubmitEvent event )
    {
        _suggestDataSource.indexDocument( event.getIdSuggestSubmit( ), IndexerAction.TASK_CREATE );
    }

    /**
     * Queues the deletion of a removed suggest submit.
     *
     * @param event
     *            the suggest submit event
     */
    public void onRemoved( @ObservesAsync @Type( EventAction.REMOVE ) SuggestSubmitEvent event )
    {
        _suggestDataSource.deleteDocument( event.getIdSuggestSubmit( ) );
    }

    /**
     * Queues the reindexing of a suggest submit whose workflow state changed.
     *
     * @param event
     *            the resource event fired by the workflow
     */
    public void onWorkflowAction( @Observes @Type( EventAction.UPDATE ) ResourceEvent event )
    {
        if ( SuggestSubmit.RESOURCE_TYPE.equals( event.getTypeResource( ) ) )
        {
            int nIdSuggestSubmit = NumberUtils.toInt( event.getIdResource( ), -1 );

            if ( nIdSuggestSubmit > 0 )
            {
                _suggestDataSource.indexDocument( nIdSuggestSubmit, IndexerAction.TASK_CREATE );
            }
        }
    }
}
