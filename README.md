[![Build Status](https://travis-ci.org/yishnish/java-maven-travis.svg?branch=master)](https://travis-ci.org/yishnish/java-maven-travis)    [![Coverage Status](https://coveralls.io/repos/github/yishnish/java-maven-travis/badge.svg?branch=master)](https://coveralls.io/github/yishnish/java-maven-travis?branch=master)

###### Maybe this makes it easier to start up a java/maven/travis/coveralls project?

1. Check out this project
2. Create a git repo for your new project
3. Go to https://travis-ci.org and add your new project repository
4. Do the same at https://coveralls.io
5. Run rename.sh with your project name as the only parameter (`sh rename.sh my-awesome-project`) this:
* renames the project and organization in your pom.xml
* renames the links to travis and coveralls in this README
* changes the remote origin path in .git/config to point at the new repo
* changes the folder name for your local repo
  * so you should `cd` to your project directory after running the script (you're already there but it looks like you aren't)
6. Probably delete this file after that


