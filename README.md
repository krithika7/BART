BART Train Schedule Android Application

I. The application
This is a very simple application to display Caltrain schedules in the Bay Area. It uses the bart open api to which you make HTTP requests and get XML data. (http://www.bart.gov/schedules/developers )
The application has three functionalities.

1.	Look up arrival times of trains and their routes at a station entered by user
2.	Look up upcoming trip time from station A to B entered by user
3.	Look up all routes and all stations in a route

The device/phone needs to be connected to the internet in order for the app to be fully functional.


II. Your Station
Get list of all Caltrain stations from the Spinner view (drop down menu). Get train arrival times by clicking the ‘get timings’ button. All times and corresponding routes are displayed in a ListView.



III. Your Trip
Enter two stations ‘from’ and ‘to’ from the Spinner menus and click ‘get timings’ to get upcoming trips from origin to destination, including transfers.



IV. Routes
Explore all Caltrain routes from a drop down menu. Get all stations in the selected route from start to end.
