function callGreet () {
    var today = new Date();
    var hourNow = today.getHours();
    var greeting;

    if (hourNow > 18 && hourNow < 24) {
        greeting = "Good evening";
    } else if (hourNow > 12 && hourNow < 18) {
        greeting = "Good afternoon";
    } else if (hourNow > 0 && hourNow < 12) {
        greeting = "Good morning";
    } else {
        greeting = "Welcome";
    }
    document.getElementById("welcome-msg").innerHTML = greeting;
};
callGreet();

function meal () {
    var today = new Date();
    var hourNow = today.getHours();
    var meal;

    if (hourNow > 16 && hourNow < 20) {
        meal = "Dinner";
    } else if (hourNow > 11 && hourNow < 14) {
    	meal = "Lunch";
    } else if (hourNow > 4 && hourNow < 10) {
    	meal = "Breakfast";
    } else {
    	meal = "Restaurants";
    }
    document.getElementById("meal").innerHTML = meal;
};
meal();