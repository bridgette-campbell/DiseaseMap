# DiseaseMap
Team 1 - Bridgette Campbell, Daniel McBride, Matt Qunell

Features implemented: 
Create an account and log in, 
download disease data from Centers for Disease Control's National Notifiable Diseases Surveillance System to display to the user, 
choose from a list of diseases to view, 
choose a time period of the disease to view, 
share information about a disease via the share icon button, 
choose color preferences, 
save login status and email to device (Shared Preferences) and save color preferences to the device database (SQLite -> UserPrefs.db).

User stories implemented: 
As a user, I can create an account and sign in. 
As a user, I can specify a time period and disease so that I have access to more specific and/or relevant information. 
As a user, I can choose a color scheme so that the app is visually appealing to me. 
As a(n aspiring) CDC worker, I can view current information so that I know where I’m needed.
As a user, I can use my credentials so that my color preferences are backed up. 

Bugs/issues:  We were not able to implement Google's Sign In, so we had to change a couple of user stories. 
Now, instead of logging in with Google and backing color preferences up we have a custom sign in and we save 
the chosen colors to the user's device. We did not replace the list of diseases with a searchable spinner due to time and we felt this was less prone to user error.