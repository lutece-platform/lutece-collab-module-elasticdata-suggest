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

import java.util.Map;

import fr.paris.lutece.plugins.elasticdata.business.AbstractDataObject;

/**
 * Suggest submit document indexed in Elasticsearch.
 */
public class SuggestSubmitDataObject extends AbstractDataObject
{
    private int _nIdSuggestSubmit;
    private String _strTitle;
    private String _strContent;
    private String _strState;
    private String _strWorkflowState;
    private String _strType;
    private String _strCategory;
    private int _nVoteCount;
    private int _nScore;
    private int _nCommentCount;
    private int _nViewCount;
    private boolean _bPinned;
    private boolean _bReported;
    private Map<String, String> _mapAuthor;

    /**
     * Returns the suggest submit id.
     *
     * @return the suggest submit id
     */
    public int getIdSuggestSubmit( )
    {
        return _nIdSuggestSubmit;
    }

    /**
     * Sets the suggest submit id.
     *
     * @param nIdSuggestSubmit
     *            the suggest submit id
     */
    public void setIdSuggestSubmit( int nIdSuggestSubmit )
    {
        _nIdSuggestSubmit = nIdSuggestSubmit;
    }

    /**
     * Returns the title.
     *
     * @return the title
     */
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * Sets the title.
     *
     * @param strTitle
     *            the title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Returns the content, as plain text.
     *
     * @return the content, as plain text
     */
    public String getContent( )
    {
        return _strContent;
    }

    /**
     * Sets the content, as plain text.
     *
     * @param strContent
     *            the content, as plain text
     */
    public void setContent( String strContent )
    {
        _strContent = strContent;
    }

    /**
     * Returns the publication state.
     *
     * @return the publication state
     */
    public String getState( )
    {
        return _strState;
    }

    /**
     * Sets the publication state.
     *
     * @param strState
     *            the publication state
     */
    public void setState( String strState )
    {
        _strState = strState;
    }

    /**
     * Returns the workflow state.
     *
     * @return the workflow state
     */
    public String getWorkflowState( )
    {
        return _strWorkflowState;
    }

    /**
     * Sets the workflow state.
     *
     * @param strWorkflowState
     *            the workflow state
     */
    public void setWorkflowState( String strWorkflowState )
    {
        _strWorkflowState = strWorkflowState;
    }

    /**
     * Returns the submit type.
     *
     * @return the submit type
     */
    public String getType( )
    {
        return _strType;
    }

    /**
     * Sets the submit type.
     *
     * @param strType
     *            the submit type
     */
    public void setType( String strType )
    {
        _strType = strType;
    }

    /**
     * Returns the category.
     *
     * @return the category
     */
    public String getCategory( )
    {
        return _strCategory;
    }

    /**
     * Sets the category.
     *
     * @param strCategory
     *            the category
     */
    public void setCategory( String strCategory )
    {
        _strCategory = strCategory;
    }

    /**
     * Returns the number of votes.
     *
     * @return the number of votes
     */
    public int getVoteCount( )
    {
        return _nVoteCount;
    }

    /**
     * Sets the number of votes.
     *
     * @param nVoteCount
     *            the number of votes
     */
    public void setVoteCount( int nVoteCount )
    {
        _nVoteCount = nVoteCount;
    }

    /**
     * Returns the score.
     *
     * @return the score
     */
    public int getScore( )
    {
        return _nScore;
    }

    /**
     * Sets the score.
     *
     * @param nScore
     *            the score
     */
    public void setScore( int nScore )
    {
        _nScore = nScore;
    }

    /**
     * Returns the number of enabled comments.
     *
     * @return the number of enabled comments
     */
    public int getCommentCount( )
    {
        return _nCommentCount;
    }

    /**
     * Sets the number of enabled comments.
     *
     * @param nCommentCount
     *            the number of enabled comments
     */
    public void setCommentCount( int nCommentCount )
    {
        _nCommentCount = nCommentCount;
    }

    /**
     * Returns the number of views.
     *
     * @return the number of views
     */
    public int getViewCount( )
    {
        return _nViewCount;
    }

    /**
     * Sets the number of views.
     *
     * @param nViewCount
     *            the number of views
     */
    public void setViewCount( int nViewCount )
    {
        _nViewCount = nViewCount;
    }

    /**
     * Returns whether the submit is pinned.
     *
     * @return whether the submit is pinned
     */
    public boolean isPinned( )
    {
        return _bPinned;
    }

    /**
     * Sets whether the submit is pinned.
     *
     * @param bPinned
     *            whether the submit is pinned
     */
    public void setPinned( boolean bPinned )
    {
        _bPinned = bPinned;
    }

    /**
     * Returns whether the submit is reported.
     *
     * @return whether the submit is reported
     */
    public boolean isReported( )
    {
        return _bReported;
    }

    /**
     * Sets whether the submit is reported.
     *
     * @param bReported
     *            whether the submit is reported
     */
    public void setReported( boolean bReported )
    {
        _bReported = bReported;
    }

    /**
     * Returns the author info.
     *
     * @return the author info
     */
    public Map<String, String> getAuthor( )
    {
        return _mapAuthor;
    }

    /**
     * Sets the author info.
     *
     * @param mapAuthor
     *            the author info
     */
    public void setAuthor( Map<String, String> mapAuthor )
    {
        _mapAuthor = mapAuthor;
    }
}
