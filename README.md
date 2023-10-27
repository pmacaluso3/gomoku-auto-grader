# Semi-automated gomoku grading

The goal of this project is to semi-automate the process of grading applicants' gomoku submissions.

## Usage overview
These steps presume that `test-report-receiver-backend` has been deployed (along with its database), and `client` has been deployed and pointed at it.

1. Applicants express interest to us, and send us their personal email that they want to submit an application from.
1. Admin logs in and goes to Create Applicants, entering just the applicant's email. (Coming soon: bulk creation from csv)
1. Admin goes to All Applicants, filters for applicants who have not set up their account, and clicks Send Setup Emails. Notes on setting up email sending: https://www.tothenew.com/blog/step-by-step-guide-sending-emails-in-spring-boot/, https://knowledge.workspace.google.com/kb/how-to-generate-an-app-passwords-000009237
1. Applicant gets a link that includes their setup token, and sets up their name & password at that link
1. Applicant writes a gomoku solution and zips their App.java plus any other classes they wrote into a zip file (instructions coming soon). They go to Create Submission and upload the zip file. They can then see this submission (plus any previous ones) in My Submissions
1. Admin logs in and goes to All Submissions. Filter by Ungraded, then click Download Zips. Unzip the downloaded file into `applicant-zips` within this repo locally.
1. Admin sets up `.env` (see `.env.sample`), then runs `node ./scripts/run-tests.js`. This creates a new copy of the `gomoku-test-suite` in `unzipped-submissions` for each applicant zip, extracts the applicant's solution into it, and runs the test suite. The test suite sends its results to the backend.
1. In All Submission, admin can now see that the submission has been graded: it has a `gradedAt`, and numbers of passing & failing tests. Click `Inspect` to look at the individual test cases.
1. Admin can Modify a test case if they think that the outcome isn't representative: for example, the applicant's submission handles player selection just fine but expects you to hit enter twice after entering the player name, while the test suite only expects one press. Note that an individual test case outcome records whether it has been manually edited or not.
1. Some tests report their final board state for visual inspection. These don't contain any assertions, so they will always start off in the passing state. Admin can look at those and mark them as failed if they don't look right. If desired, more tests could report their board state in addition to their pass/fail value.
1. Once manual review is done, admin can export the list of passing applicants (coming soon) and archive the old submissions (coming soon).
1. Admin should run `node scripts/cleanup.js` to clean out the temp directories within this local repo.


TODO:
    - deployment?
    - stable jwt secret
    - coming soon's from readme
    - timezone in formatDateTime
