/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package org.guvnor.asset.management.service;

import java.util.List;

import org.guvnor.asset.management.model.RepositoryStructureModel;
import org.guvnor.common.services.project.model.GAV;
import org.guvnor.common.services.project.model.POM;
import org.guvnor.common.services.project.model.Project;
import org.guvnor.structure.repositories.Repository;
import org.jboss.errai.bus.server.annotations.Remote;
import org.uberfire.backend.vfs.Path;

@Remote
public interface RepositoryStructureService {

    Path initRepositoryStructure( final GAV gav, final Repository repo);

    Path initRepositoryStructure( POM pom, String baseUrl, Repository repo, boolean multiProject );

    Repository initRepository( final Repository repo, boolean managed );

    Path convertToMultiProjectStructure( final List<Project> projects,
            final GAV parentGav,
            final Repository repo,
            final boolean updateChildrenGav,
            final String comment );

    RepositoryStructureModel load( final Repository repository );


    RepositoryStructureModel load( final Repository repository, boolean includeModules );

    void save( final Path pathToPomXML,
            final RepositoryStructureModel model,
            final String comment );

    boolean isValidProjectName( String name );

    boolean isValidGroupId( String groupId );

    boolean isValidArtifactId( String artifactId );

    boolean isValidVersion( String version );

    void delete( final Path pathToPomXML, final String comment );

}
