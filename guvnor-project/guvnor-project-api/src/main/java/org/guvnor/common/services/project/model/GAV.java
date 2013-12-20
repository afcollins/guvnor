/*
 * Copyright 2011 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.guvnor.common.services.project.model;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class GAV {

    private String groupId;
    private String artifactId;
    private String version;

    public GAV() {
        this.groupId = null;
        this.artifactId = null;
        this.version = null;
    }

    public GAV( String groupId,
                String artifactId,
                String version ) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setGroupId( String groupId ) {
        this.groupId = groupId;
    }

    public void setArtifactId( String artifactId ) {
        this.artifactId = artifactId;
    }

    public void setVersion( String version ) {
        this.version = version;
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + ":" + version;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) {
            return true;
        }
        if ( !( o instanceof GAV ) ) {
            return false;
        }

        GAV gav = (GAV) o;

        if ( artifactId != null ? !artifactId.equals( gav.artifactId ) : gav.artifactId != null ) {
            return false;
        }
        if ( groupId != null ? !groupId.equals( gav.groupId ) : gav.groupId != null ) {
            return false;
        }
        if ( version != null ? !version.equals( gav.version ) : gav.version != null ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = groupId != null ? groupId.hashCode() : 0;
        result = 31 * result + ( artifactId != null ? artifactId.hashCode() : 0 );
        result = 31 * result + ( version != null ? version.hashCode() : 0 );
        return result;
    }
}