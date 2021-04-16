<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>File Upload Example</title>
</head>
<body>

<!-- 
<form action="/upload" method="post">
	<input type="hidden" name="imageSource" id="imageSource">
 <input type="submit" value="Submit">
</form> -->

<script>
function statusChangeCallback(response) {
          if (response.status === 'connected') {
                       testAPI();
                       storeUserID();
                        getphotos();
   	} else if (response.status === 'not_authorized') {
                  	document.getElementById('status').innerHTML = 'Please log ' +'into this app.';
   	} else {
         	         	document.getElementById('status').innerHTML = 'Please log ' + 'into Facebook.';
   	}
}
function checkLoginState() {
   	FB.getLoginStatus(function(response) {
             statusChangeCallback(response);
   	});
}
window.fbAsyncInit = function() {
FB.init({
   	appId :'360487201910612',
   	cookie : true, // enable cookies to allow the server to access
   	// the session
   	xfbml : true, // parse social plugins on this page
   	version : 'v2.1' // use version 2.1
});
FB.AppEvents.logPageView(); 
FB.getLoginStatus(function(response) {
   	statusChangeCallback(response);
   	});
};
// Load the SDK asynchronously
(function(d, s, id) {
   	var js, fjs = d.getElementsByTagName(s)[0];
   	if (d.getElementById(id)) return;
   	js = d.createElement(s); js.id = id;
   	js.src = "//connect.facebook.net/en_US/sdk.js";
   	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));
function testAPI() {
   	console.log('Welcome! Fetching your information.... ');
   	FB.api('/me', function(response) {
         	console.log('Successful login for hello: ' + response.name);
         	 document.getElementById('startapp').type = 'submit';
   	});
}
function storeUserID() {
    var userID;
    console.log('Welcome!  Fetching your information.... in storeUserID');
    FB.api('/me', function (response) {
        console.log('Successful login for: ' + response.name + '\n userid:' + response.id);
        userID = response.id;
        document.getElementById('userID').value = userID;
    });
}

function getphotos() {
	console.log('Welcome!  Fetching your information.... StoreImages');
    var imageLinks = new Array();
    var imageID = new Array();
    FB.api('me/albums?fields=photos.limit(10){webp_images}&limit=5', function (response) {
        var albums = response.data;
        albums.forEach(album => {
            console.log("album")
            console.log(album)
            var photos = album.photos
            console.log("\nphotos:")
            console.log(photos)
            if (photos != undefined) {
                photos.data.forEach(photo => {
                    imageLinks.push(photo.webp_images[0].source)
                    imageID.push(photo.id)
                });
            }
        });
        document.getElementById('imageLinks').value = imageLinks;
        document.getElementById('imageID').value = imageID;
        console.log(imageID);
        console.log(imageLinks);
    });
}

</script>

<form id="form_home" action="/upload" method="post" >
            <input type="hidden" name="userID" id="userID">
            <input type="hidden" name="imageLinks" id="imageLinks">
            <input type="hidden" name="imageID" id="imageID">
            <%--    <div id="status"></div>--%>
            <input id="startapp" type="hidden" class="btn btn-default btn-block" value="Start the Process">

        </form>
<fb:login-button scope="public_profile,email" onlogin="checkLoginState();">
</fb:login-button>
<div id="status">
</div>
<div id="imageSource"></div>
<div id="imageID"></div>
<script async defer crossorigin="anonymous" src="https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v8.0&appId=1121389311654849&autoLogAppEvents=1" nonce="iGO5Dyev"></script>

<!-- <div id="imageDiv" class="thumbnail"> </div> -->
<div id="fb-age">
</div>

<div id="theText"></div>

</body>
</html>
