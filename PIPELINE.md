# Jenkins Pipeline

## Overview

The Jenkinsfile in this repository contains a sample pipeline that performs a
build and applies object configurations to instantiate this application.

The pipeline can be used to maintain a running application on an OpenShift
cluster.

## Basic: Getting Started

1. Start an OpenShift cluster (origin v1.3 and later). Create a new project.
2. [Optional] Create a fork of this repository
3. Instantiate the pipeline from template by invoking:
   (Where FORK is your own fork of this repository or `openshift-roadshow`)
   ```
   oc new-app -f https://raw.githubusercontent.com/FORK/nationalparks/master/ose3/pipeline-buildconfig-template.json -p GIT_URI=https://github.com/FORK/nationalparks.git
   ```

4. Start the pipeline build from the CLI with:
   ```
   oc start-build nationalparks-pipeline
   ```

   OR

   Start the pipeline from the web console. Navigate to the project, Builds->Pipelines.
   Click `Start` next to the *nationalparks-pipeline*.


## Advanced: Example of a PR-based development flow

The Jenkinsfile in this repository can be used to support a PR flow with Jenkins on OpenShift.
The main application is built and deployed to an 'integration' project. Every time a PR is
submitted to the source repository, a new OpenShift project is created to build and test the
PR and allow for manual testing. Once the manual testing is approved, the build succeeds.

The following steps guide you through the setup of the Github Organization Folder plugin in
Jenkins and the required steps on the GitHub side. The Github Organization Folder plugin
instantiates a pipeline for every project it finds with a Jenkinsfile at its root. It also
sets up webhooks in GitHub so that new PRs and changes to a branch are automatically built
in Jenkins. In order for webhooks to work, the ngrok application is used to create a public
endpoint for the internal Jenkins service.

### GitHub Setup

1. Create your own forks of [nationalparks](https://github.com/openshift-roadshow/nationalparks.git),
   [mlbparks](https://github.com/openshift-roadshow/mlbparks.git), and
   [parksmap-web](https://github.com/openshift-roadshow/parksmap-web.git).
2. Create a [GitHub personal access token](https://help.github.com/articles/creating-an-access-token-for-command-line-use/)
   with access to `repo`, `admin:repo_hook`, `admin:org_hook`.
   (Keep your token to use below as GITHUB-TOKEN).

### OpenShift/Jenkins Setup

1. Create a new project:
   ```
   oc new-project PROJECT-NAME
   ```

2. Assign the right permission to your jenkins service account:
   1. Login as system administrator. If running cluster up:

   ```
   oc login -u system:admin
   ```

   2. Apply the self-provisioner role to your jenkins service account (Substitute PROJECT-NAME with the name of the project you created above):

   ```
   oc adm policy add-cluster-role-to-user self-provisioner system:service-account:PROJECT-NAME:jenkins
   ```

   3. Log back in as a regular user. If using cluster up:

   ```
   oc login -u developer
   ```

3. Add Jenkins to your project: 

   ```
   oc new-app openshift/jenkins-ephemeral -p MEMORY_LIMIT=1Gi
   ```

4. [Optional] If your cluster is not externally accessible (if for example, you started it locally with `cluster up`): 
   1. Expose the jenkins service externally (for GitHub webhooks) with ngrok: 

      ```
      oc new-app -f https://raw.githubusercontent.com/csrwng/ngrok/master/openshift/ngrok-template.yaml -p HOST=jenkins -p PORT=80
      ```

   2. Get public webhook URL from ngrok:
      1. Navigate to the ngrok application using the route created by the ngrok template:
          ```
          oc get route ngrok
          ```

      2. The main page will display 2 URLs. Copy either URL to use for your Jenkins webhook.

   3. Configure the Github webhook URL on Jenkins
      1. Login to Jenkins
      2. Go to Manage Jenkins -> Configure System
      3. Under GitHub section:

         1. Click on the `Advanced...` button
         2. Check `Specify another hook url for GitHub configuration`
         3. Replace the default URL host with the one from ngrok (url should look something like: http://47e8e09b.ngrok.io/github-webhook/)

      4. Click on `Save` at the bottom of the screen.

5. Configure Github Server on Jenkins
   1. Login to Jenkins
   2. Go to Manage Jenkins -> Configure System
   3. Under GitHub section:

      1. Click on `Add Github Server` and select `GitHub Server`
      2. Click on `Add` next to `Credentials` and select `Jenkins`
      3. Leave `Kind` as `Username with password`. For `Username` enter your GitHub username, for `Password` enter your GITHUB-TOKEN, click on `Add`
      4. Click on `Add` again (as in step b)
      5. Select a `Kind` of `Secret text`. For `Secret` paste your GITHUB-TOKEN, and for `Description` enter descriptive text like `Github Token`, click on `Add`
      6. Under the `Credentials` dropdown, select the one you just created.
      7. Click on `Test connection`

   4. Click on `Save` at the bottom of the screen.
6. Add Github Organization Plugin
   1. Click on Manage Jenkins -> Manage Plugins -> Available
   2. In the `Filter` search box, enter `github organization`
   3. Check the box next to `GitHub Organization Folder Plugin`
   4. Click on `Download now and install after restart`
   5. Check the checkbox next to `Restart Jenkins when installation is complete and no jobs are running`
   6. Log back in to Jenkins and navigate to the Home screen by clicking on `Jenkins` in the top left of the window
7. Add the organization
   1. Click on `New Item`
   2. Enter a name for your organization project
   3. Select `Github Organization` and click `OK`
   3. Under `Project Sources` -> `Github Organization`
      1. For `Owner` enter your GitHub username
      2. For Scan credentials, select the credentials you created earlier.
      3. For repository name pattern, enter a pattern that includes the repositories you forked earlier: `(parksmap-web|nationalparks|mlbparks)`
   4. Click on `Save` at the bottom

Once Jenkins instantiates the pipelines under the Organization folder, a build will be started for
each repository found by the plugin. Their corresponding OpenShift artifacts will be instantiated
in the same project as Jenkins. This will contain the `master` branch of each project. Each new PR
to each repository will result in a new pipeline getting instantiated and a new project getting created
for that pipeline. The GitHub PR will then include a status of the build, a link to the application preview,
and a link to the approval page.
