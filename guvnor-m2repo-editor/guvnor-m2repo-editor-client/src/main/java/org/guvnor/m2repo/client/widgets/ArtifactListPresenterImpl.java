/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.guvnor.m2repo.client.widgets;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import org.guvnor.m2repo.model.JarListPageRequest;
import org.guvnor.m2repo.model.JarListPageRow;
import org.guvnor.m2repo.service.M2RepoService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.paging.PageResponse;
import org.uberfire.workbench.events.NotificationEvent;

@Dependent
public class ArtifactListPresenterImpl
        implements ArtifactListPresenter {

    static final boolean DEFAULT_ORDER_ASCENDING = true;
    private final ArtifactListView view;

    private final Caller<M2RepoService> m2RepoService;

    private final Event<NotificationEvent> notification;

    private RefreshableAsyncDataProvider dataProvider;

    @Inject
    public ArtifactListPresenterImpl( ArtifactListView view,
                                      Caller<M2RepoService> m2RepoService,
                                      Event<NotificationEvent> notification ) {
        this.view = view;
        this.m2RepoService = m2RepoService;
        this.notification = notification;
    }

    @PostConstruct
    public void init() {
        view.init( this );
        dataProvider = new RefreshableAsyncDataProvider( view, m2RepoService );
        dataProvider.addDataDisplay( view.getDisplay() );
    }

    @Override
    public ArtifactListView getView() {
        return view;
    }

    @Override
    public void refresh() {
        dataProvider.refresh();
        notification.fire( new NotificationEvent( view.getRefreshNotificationMessage() ) );
    }

    @Override
    public void search( final String filter ) {
        dataProvider.setFilter( filter );
        refresh();
    }

    @Override
    public void onOpenPom( String path ) {
        m2RepoService.call( new RemoteCallback<String>() {
            @Override
            public void callback( final String response ) {
                view.showPom( response );
            }
        } ).getPomText( path );
    }

    /**
     * Extension to AsyncDataProvider that supports refreshing the table.
     */
    private static class RefreshableAsyncDataProvider extends AsyncDataProvider<JarListPageRow> {

        private final ArtifactListView view;
        private final Caller<M2RepoService> m2RepoService;
        private String filter;

        protected RefreshableAsyncDataProvider( final ArtifactListView view,
                                                final Caller<M2RepoService> m2RepoService ) {
            this.view = PortablePreconditions.checkNotNull( "view",
                                                            view );
            this.m2RepoService = PortablePreconditions.checkNotNull( "m2RepoService",
                                                                     m2RepoService );
        }

        protected void setFilter( String filter ) {
            this.filter = filter;
        }

        protected void refresh() {
            for ( HasData<JarListPageRow> display : getDataDisplays() ) {
                onRangeChanged( display );
            }
        }

        @Override
        protected void onRangeChanged( HasData<JarListPageRow> display ) {
            final Range range = display.getVisibleRange();
            JarListPageRequest request = new JarListPageRequest( range.getStart(),
                                                                 range.getLength(),
                                                                 filter,
                                                                 getSortColumnDataStoreName(),
                                                                 isSortColumnAscending() );

            m2RepoService.call( new RemoteCallback<PageResponse<JarListPageRow>>() {
                @Override
                public void callback( final PageResponse<JarListPageRow> response ) {
                    updateRowCount( response.getTotalRowSize(),
                                    response.isTotalRowSizeExact() );
                    updateRowData( response.getStartRowIndex(),
                                   response.getPageRowList() );
                }
            } ).listArtifacts( request );
        }

        private String getSortColumnDataStoreName() {
            final ColumnSortList columnSortList = view.getColumnSortList();
            return ( columnSortList == null || columnSortList.size() == 0 ) ? null : columnSortList.get( 0 ).getColumn().getDataStoreName();
        }

        private boolean isSortColumnAscending() {
            final ColumnSortList columnSortList = view.getColumnSortList();
            return ( columnSortList == null || columnSortList.size() == 0 ) ? DEFAULT_ORDER_ASCENDING : columnSortList.get( 0 ).isAscending();
        }
    }
}
