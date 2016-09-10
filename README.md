# XaaS-Packager

Uses the Topology and Orchestration Specification for Cloud Applications (TOSCA) to wrap arbitrary applications into an deployable and provisionable CSAR archive


#### Build 
Run 'mvn package'

#### Run
Deploy on Tomcat, open up {host}:{port}/XaaSPackager

#### Dependencies
Eclipse Winery with CSARs

#### Limitations
Right now topologies must be specified under /src/main/resources/deploymentArtifactTopologies.xml to make them availabe inside the packager.
