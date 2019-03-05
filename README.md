# SYSC4806
[![Build Status](https://travis-ci.org/DanGravel/SYSC4806.svg?branch=master)](https://travis-ci.org/DanGravel/SYSC4806)
# PROJECT
**Summary**

Article Submitter submits Article (some attachment, any format is ok). The Editor assigns the article to a Reviewer, and provides a deadline for submission of the review. The Reviewer submits a Review of the article. The Editor accepts/rejects the Article based on the review. The status of the article ("submitted", "in review", "accepted", "rejected") and the review should be visible to the Editor and Submitter. The Editor can list all articles in the system, sort/filter them by status, and highlight/show articles whose reviews are overdue. The Reviewer can list the articles assigned to him/her, and sort them by deadline.

**Current State**

The current features that have been implemented into the system are:
- Login and sign up pages
- Page to upload documents to the system for submitters
- Allow editors to assign files to a reviewer
- Reviewers can download the file to look over it and post a review for the file
- The editor can use the review to accept or reject the file

The project also has CI integration with Travis CI. There are unit tests written to help with the process. 

**DB Schema**

The DB used by us currently is a memory DB through a CrudRespository. We have created multiple models and made repositories for the models. 
The schema for those models can be seen below (Made to look like a sql database schema):

Table: User

Description: Holds information on the User. The user can have a role of Reviewer, Submitter or Editor.

| Name      | Type    | Key      |
|:---------:|:-------:|:--------:|
| id        | int     | Primary  |
| password  | string  | N/A      |
| role      | string  | N/A      |
 
Table: Article

Description: Holds Article information. Keeps track of the state of the review.

| Name      | Type    | Key      |
|:---------:|:-------:|:--------:|
| id        | int     | Primary  |
| filename  | string  |          |
| filetype  | string  |          |
| review    | string  |          |
| state     | string  |          |   
| date      | Date    |          |   
| data      | Blob    |          |

Table: Article_User

Description: Many-To-Many table between Users and Articles. Multiple users will be associated to a file and a User can also have many files hence the Many-To-Many relationship.

| Name        | Type    | Key      |
|:---------  :|:-------:|:--------:|
| article_id  | int     | foreign  |
| user_id     | int     | foreign  |