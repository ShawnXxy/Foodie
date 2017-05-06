(function() {

	/***********
	 * Variables
	 **************/
	var user_id = '';
	var user_fullname = '';
	var lon = -74.009508;
	var lat = 40.7393257;

	/********************
	 * INITIALING PAGE
	 ***********************/
	function init() {
		// Register event liseners.
		newDOM('register-btn').addEventListener('click', register);
		newDOM('login-btn').addEventListener('click', login);// login() defined at LOGIN SETTING
		newDOM('nearby-btn').addEventListener('click', loadNearbyRestaurants);// defined in RESTAURANTS SETTING
		newDOM('fav-btn').addEventListener('click', loadFavoriteRestaurants); // defined in RESTAURANTS SETTING
		newDOM('recommend-btn').addEventListener('click',
				loadRecommendedRestaurants); // defined in RESTAURANTS SETTING

		 validateSession(); // defined at SESSION SETTING
		// fake users info used for test
//		onSessionValid({
//			user_id : '1111',
//			name : 'Xiangyu Xiao'
//		});
	} // end of init()

	/*******************
	 * SESSION SETTING
	 *********************/
	function validateSession() {
		// The request parameters
		var url = './LoginServlet';
		var req = JSON.stringify({});

		// display loading message
		showLoadingMessage('Validating session...'); // defined at DISPLAY SETTING

		// make AJAX call
		ajax('GET', url, req, function(res) {
		// session is still valid	
			var result = JSON.parse(res);
			if (result.status === 'OK') {
				onSessionValid(result);
			}
		}); // end ajax call
	} // end of validateSession()

	function onSessionValid(result) {
		user_id = result.user_id;
		user_fullname = result.name;

		var loginForm = newDOM('login-form');
		var restaurantNav = newDOM('restaurant-nav');
		var restaurantList = newDOM('restaurant-list');
		var avatar = newDOM('avatar');
		var welcomeMsg = newDOM('welcome-msg');
		var logoutBtn = newDOM('logout-link');

		function callGreet() {
			var greeting;
		    var today = new Date();
		    var hourNow = today.getHours();
		    if (hourNow > 18) {
		        greeting = "Good evening, " + user_fullname;
		    } else if (hourNow > 12) {
		        greeting = "Good afternoon, " + user_fullname;
		    } else if (hourNow > 0) {
		        greeting = "Good morning, " + user_fullname;
		    } else {
		        greeting = "Welcome";
		    }
		    document.getElementById("welcome-msg").innerHTML = greeting;
		};
		callGreet();
//		welcomeMsg.innerHTML = greeting + ', ' + user_fullname;
//		welcomeMsg.innerHTML = 'Welcome, ' + user_fullname;

		// if login successfull, show content
		// showElement() & hideElement() are defined at DISPLAY SETTING
		showElement(restaurantNav);
		showElement(restaurantList);
		showElement(avatar);
		showElement(welcomeMsg);
		showElement(logoutBtn, 'inline-block');
		hideElement(loginForm);

		initGeoLocation();// defined at GEO INFO SETTING
	} // end of onSessionValid()

	function onSessionInvalid() {
		var loginForm = newDOM('login-form');
		var restaurantNav = newDOM('restaurant-nav');
		var restaurantList = newDOM('restaurant-list');
		var avatar = newDOM('avatar');
		var welcomeMsg = newDOM('welcome-msg');
		var logoutBtn = newDOM('logout-link');
		
		// hide content if failed logged in
		hideElement(restaurantNav);
		hideElement(restaurantList);
		hideElement(avatar);
		hideElement(logoutBtn);
		hideElement(welcomeMsg);
		// show login form
		showElement(loginForm);
	}
	
	/*****************************
	 * GEOLOCATION INIT SETTING
	 *******************************/
	function initGeoLocation() {
		if (navigator.geolocation) { //https://dev.w3.org/geo/api/spec-source.html
			navigator.geolocation.getCurrentPosition(onPositionUpdated,
					onLoadPositionFailed, {
						maximumAge : 60000 // By using the 'maximumAge' option above, the position object is guaranteed to be at most 10 minutes old.
					});
			showLoadingMessage('Retrieving your location...'); // function is defined at DISPLAY SETTING
		} else {
			onLoadPositionFailed(); // function is defined below
		}
	}

	function onPositionUpdated(position) {
		lat = position.coords.latitude;
		lon = position.coords.longitude;

		loadNearbyRestaurants();
	}

	function onLoadPositionFailed() {
		console.warn('navigator.geolocation is not available');
		// loadNearbyRestaurants();
		getLocationFromIP();
	}

	function getLocationFromIP() {
		// Get location from http://ipinfo.io/json
		var url = 'http://ipinfo.io/json'
		var req = null;
		ajax('GET', url, req, function(res) {
			// session is still valid
			var result = JSON.parse(res);
			if ('loc' in result) {
				var loc = result.loc.split(',');
				lat = loc[0];
				lon = loc[1];
			} else {
				console.warn('Getting location by IP failed.');
			}
			loadNearbyRestaurants(); // function defined in below at RESTAURANTS part
		}); // end of ajax
	} // end getLocationFromIP()

	/********************
	 * LOGIN SETTING
	 **********************/
	function login() {
		var username = newDOM('username').value;
		var password = newDOM('password').value;
		// MD5 hash
		password = md5(username + md5(password));

		// The request parameters
		var url = './LoginServlet';
		var params = 'user_id=' + username + '&password=' + password;
		var req = JSON.stringify({});

		ajax('POST', url + '?' + params, req, function(res) {
		// successful callback	
			var result = JSON.parse(res);
			// successfully logged in
			if (result.status === 'OK') {
				onSessionValid(result);
			}
		}, function() {
			showLoginError(); // defined below in this session
		}); // end ajax call
	} // end login()

	function showLoginError() {
		newDOM('login-error').innerHTML = 'Invalid username or password';
	}

	function clearLoginError() {
		newDOM('login-error').innerHTML = '';
	}

	function register() {
		var username = newDOM('username').value;
		var password = newDOM('password').value;
		// MD5 hash
		password = md5(username + md5(password));

		// The request parameters
		var url = './Register';
		var params = 'user_id=' + username + '&password=' + password;
		var req = JSON.stringify({});

		ajax('POST', url + '?' + params, req, function(res) {
		// successful callback	
			var result = JSON.parse(res);
			// successfully logged in
			if (result.status === 'OK') {
				onSessionValid(result);
			}
		}, function() {
			showLoginError(); // defined below in this session
		}); // end ajax call
	} // end login()
	/***************************************************
	 * DISPLAY SETTING
	 * 
	 * @param btnId -
	 *            The id of the navigation button
	 *********************************************************/
	function activeBtn(btnId) {
		var btns = document.getElementsByClassName('main-nav-btn');

		// deactivate all navigation buttons
		for (var i = 0; i < btns.length; i++) {
			btns[i].className = btns[i].className.replace(/\bactive\b/, '');
		}

		// active the one that has id = btnId
		var btn = newDOM(btnId);
		btn.className += ' active';
	} // end activeBtn()

	function showLoadingMessage(msg) {
		var restaurantList = newDOM('restaurant-list');
		restaurantList.innerHTML = '<p class="notice"><i class="fa fa-spinner fa-spin"></i> '
				+ msg + '</p>';
	}

	function showWarningMessage(msg) {
		var restaurantList = newDOM('restaurant-list');
		restaurantList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-triangle"></i> '
				+ msg + '</p>';
	}

	function showErrorMessage(msg) {
		var restaurantList = newDOM('restaurant-list');
		restaurantList.innerHTML = '<p class="notice"><i class="fa fa-exclamation-circle"></i> '
				+ msg + '</p>';
	}

	function hideElement(element) {
		element.style.display = 'none';
	}

	function showElement(element, style) {
		// var displayStyle;
		// if (style) {
		// 	displayStyle = style;
		// } else {
		// 	displayStyle = 'block';
		// }
		var displayStyle = style ? style : 'block';		
		element.style.display = displayStyle;
	}
	
	/************************************************************
	 * HELPER function
	 *
	 * newDOM() that creates a new DOM element <tag options...>
	 *
	 * ajax() connection function
	 *
	 * @param method -
	 *            GET|POST|PUT|DELETE
	 * @param url -
	 *            API end point
	 * @param callback -
	 *            This the successful callback
	 * @param errorHandler -
	 *            This is the failed callback
	 ***************************************************************/
	function newDOM(tag, options) {
		if (!options) {
			return document.getElementById(tag);
		}
		var element = document.createElement(tag);
		for ( var option in options) {
			if (options.hasOwnProperty(option)) {
				element[option] = options[option];
			}
		}
		return element;
	} // end of newDOM()

	// AJAX
	function ajax(method, url, data, callback, errorHandler) {
		var xhr = new XMLHttpRequest();

		xhr.open(method, url, true);

		xhr.onload = function() {
			switch (xhr.status) {
			case 200:
				callback(xhr.responseText);
				break;
			case 403:
				onSessionInvalid();
				break;
			case 401:
				errorHandler();
				break;
			} // end switch()
		}; // end onload()
		xhr.onerror = function() {
			console.error("The request couldn't be completed.");
			errorHandler();
		};

		if (data === null) {
			xhr.send();
		} else {
			xhr.setRequestHeader("Content-Type",
					"application/json;charset=utf-8");
			xhr.send(data);
		}
	} // end of ajax()

	/*********************************************
	 * 
	 * RESTAURANTS
	 * 
	 *******************************************/
	function loadNearbyRestaurants() {
		//console.log('loadNearbyRestaurants');
		activeBtn('nearby-btn');

		// The request parameters
		var url = './restaurants';
		var params = 'user_id=' + user_id + '&lat=' + lat + '&lon=' + lon;
		var req = JSON.stringify({});

		// display loading message
		showLoadingMessage('Loading nearby restaurants...');

		// make AJAX call
		ajax('GET', url + '?' + params, req, function(res) {
		// successful callback		
			var restaurants = JSON.parse(res);
			if (restaurants == null || restaurants.length === 0) {
				showWarningMessage('No nearby restaurant.');
			} else {
				listRestaurants(restaurants); // defined in RESTAURANTS part below
			}
		}, function() {
			showErrorMessage('Cannot load nearby restaurants.');
		}); // end ajax()
	} // end loadNearbyRestaurants()

	function loadFavoriteRestaurants() {
		activeBtn('fav-btn');

		// The request parameters
		var url = './history';
		var params = 'user_id=' + user_id;
		var req = JSON.stringify({});

		// display loading message
		showLoadingMessage('Loading favorite restaurants...');

		// make AJAX call
		ajax('GET', url + '?' + params, req, function(res) {
			var restaurants = JSON.parse(res);
			if (restaurants == null || restaurants.length === 0) {
				showWarningMessage('No favorite restaurant.');
			} else {
				listRestaurants(restaurants);
			}
		}, function() {
			showErrorMessage('Cannot load favorite restaurants.');
		}); // end ajax()
	} // end loadFavoriteRestaurants()

	function loadRecommendedRestaurants() {
		activeBtn('recommend-btn'); // defined in DISPLAY SETTING

		// The request parameters
		var url = './recommendation';
		var params = 'user_id=' + user_id;
		var req = JSON.stringify({});

		// display loading message
		showLoadingMessage('Loading recommended restaurants...'); // defined in DISPLAY SETTING

		// make AJAX call
		ajax('GET', url + '?' + params, req, function(res) {
				// successful callback			
					var restaurants = JSON.parse(res);
					if (restaurants == null || restaurants.length === 0) {
						showWarningMessage('No recommended for now. Make sure you have favorites, or try out nearby restaurants.');
					} else {
						listRestaurants(restaurants);
					}
				}, function() {
				// failed callback			
					showErrorMessage('Cannot load recommended restaurants.');
				}); // end ajax()
	} // end loadRecommendedRestaurants()

	function changeFavoriteRestaurant(business_id) {
		// Check whether this restaurant has been visited or not
		var li = newDOM('restaurant-' + business_id);
		var favIcon = newDOM('fav-icon-' + business_id);
		var isVisited = li.dataset.visited !== 'true';

		// The request parameters
		var url = './history';
		var req = JSON.stringify({
			user_id : user_id,
			visited : [ business_id ]
		});
		// var method;
		// if (isVisited) {
		// 	method = 'POST';
		// } else {
		// 	method = "DELETE";
		// }
		var method = isVisited ? 'POST' : 'DELETE';

		ajax(method, url, req, function(res) {
				// successful callback			
					var result = JSON.parse(res);
					if (result.status === 'OK') {
						li.dataset.visited = isVisited;
						//  if (isVisited) {
						// 	 favIcon.className = 'fa fa-heart';
						//  } else {
						// 	 favIcon.className = 'fa fa-heart-o';
						//  }
						favIcon.className = isVisited ? 'fa fa-heart'
								: 'fa fa-heart-o';
					}
				}); // end ajax()
	} // end changeFavoriteRestaurant()

	function listRestaurants(restaurants) {
		// Clear the current results
		var restaurantList = newDOM('restaurant-list');
		restaurantList.innerHTML = '';

		for (var i = 0; i < restaurants.length; i++) {
			addRestaurant(restaurantList, restaurants[i]);
		}
	} // end listRestaurants()

	function addRestaurant(restaurantList, restaurant) {
		var business_id = restaurant.business_id;

		// create the <li> tag and specify the id and class attributes
		var li = newDOM('li', {
			id : 'restaurant-' + business_id,
			className : 'restaurant'
		});

		// set the data attribute
		li.dataset.business = business_id;
		li.dataset.visited = restaurant.is_visited;

		// restaurant image
		li.appendChild(newDOM('img', {
			src : restaurant.image_url
		}));

		// section
		var section = newDOM('div', {});

		// title
		var title = newDOM('a', {
			href : restaurant.url,
			target : '_blank',
			className : 'restaurant-name'
		});
		title.innerHTML = restaurant.name;
		section.appendChild(title);

		// category
		var category = newDOM('p', {
			className : 'restaurant-category'
		});
		category.innerHTML = 'Category: ' + restaurant.categories.join(', ');
		section.appendChild(category);

		// stars: used for rating
		var stars = newDOM('div', {
			className : 'stars'
		});
		for (var i = 1; i <= restaurant.stars; i++) {
			var star = newDOM('i', {
				className : 'fa fa-star'
			});
			stars.appendChild(star);
		}
		if (('' + restaurant.stars).match(/\.5newDOM/)) {
			stars.appendChild(newDOM('i', {
				className : 'fa fa-star-half-o'
			}));
		}
		section.appendChild(stars);
		li.appendChild(section);

		// address
		var address = newDOM('p', {
			className : 'restaurant-address'
		});
		address.innerHTML = restaurant.full_address.replace(/,/g, '<br/>');
		li.appendChild(address);

		// favorite link
		var favLink = newDOM('p', {
			className : 'fav-link'
		});
		favLink.onclick = function() {
			changeFavoriteRestaurant(business_id);
		};

		favLink.appendChild(newDOM('i', {
			id : 'fav-icon-' + business_id,
			className : restaurant.is_visited ? 'fa fa-heart' : 'fa fa-heart-o'
		}));
		li.appendChild(favLink);
		restaurantList.appendChild(li);
	} // end of addRestaurant()

	init();

})();

// END
