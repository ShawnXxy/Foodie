//function callGreet () {
//    var today = new Date();
//    var hourNow = today.getHours();
//    var greeting;
//
//    if (hourNow > 18) {
//        greeting = "Good evening";
//    } else if (hourNow > 12) {
//        greeting = "Good afternoon";
//    } else if (hourNow > 0) {
//        greeting = "Good morning";
//    } else {
//        greeting = "Welcome";
//    }
//    document.getElementById("welcome-msg").innerHTML = greeting;
//};
//callGreet();

function meal () {
    var today = new Date();
    var hourNow = today.getHours();
    var meal;

    if (hourNow > 16 && hourNow < 21) {
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