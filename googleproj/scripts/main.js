/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

// Shortcuts to DOM Elements.
var messageForm = document.getElementById('message-form');
var updateForm = document.getElementById('update-form');
var messageInput = document.getElementById('new-post-message');
var titleInput = document.getElementById('new-post-title');
var code = document.getElementById('new-post-code');
var type = document.getElementById('new-post-cat');
var desc = document.getElementById('new-post-desc');
var amount = document.getElementById('new-post-amount');
var imageUrl = document.getElementById('new-post-imageUrl');
var signInButton = document.getElementById('sign-in-button');
var signOutButton = document.getElementById('sign-out-button');
var splashPage = document.getElementById('page-splash');
var addPost = document.getElementById('add-post');
var updatePost = document.getElementById('edit-post');
var addButton = document.getElementById('add');
var recentPostsSection = document.getElementById('recent-posts-list');
var userPostsSection = document.getElementById('user-posts-list');
var topUserPostsSection = document.getElementById('top-user-posts-list');
var recentMenuButton = document.getElementById('menu-recent');
var deleteButton = document.getElementById('delete');
var myPostsMenuButton = document.getElementById('menu-my-posts');
var myTopPostsMenuButton = document.getElementById('menu-my-top-posts');
var listeningFirebaseRefs = [];
var storageRef = firebase.storage().ref();
var keyHidden=document.getElementById('updateKey');
var updateButton=document.getElementById('saveSubmit');

 var messageInputU = document.getElementById('edit-post-message');
			var titleInputU = document.getElementById('edit-post-title');
			var codeU = document.getElementById('edit-post-code');
			var typeU = document.getElementById('edit-post-cat');
			//var desc = document.getElementById('edit-post-desc');
			var amountInputU = document.getElementById('edit-post-amount');
			var imageUrlU = document.getElementById('edit-post-imageUrl');
			var editTextU = document.getElementById('edit-post-message');
var format='<html><head><style type=\"text/css\">@font-face{    font-family: Zawgyi-One;    src: url("fonts/zawgyi.ttf") }    body{   font-family: TharLon;  background-color:#f3e5f5;   color:#9c27b0;    text-align: justify;}</style></head><body>${body}</body></html>'
/**
 * Saves a new post to the Firebase DB.
 */
// [START write_fan_out]
function writeNewPost(uid, username, picture, title, body,desc,amount,code,type,imageUrl) {
  // A post entry.
    // Get a key for a new Post.
	
	
	

   var file = document.getElementById('file').files[0];
   var metadata = {
        'contentType': file.type
      };

      // Push to child path.
      // [START oncomplete]
      storageRef.child('images/' + file.name).put(file, metadata).then(function(snapshot) {
        console.log('Uploaded', snapshot.totalBytes, 'bytes.');
        console.log(snapshot.metadata);
        var url = snapshot.metadata.downloadURLs[0];
        console.log('File available at', url);
		
		
		 document.getElementById('submit1').disabled=false;
		  document.getElementById('new-post-imageUrl').value=url;
		  
		   var newPostKey = firebase.database().ref().child('posts').push().key;
			  
			  
			  var date=new Date();
			  var date_=JSON.stringify(date.toString());
			  var htmlStr=getHtml(body);
			  var discountAmount=parseFloat($('#discount-price').val());
			  var postData = {
				author: username,
				uid: uid,
				htmlDetail: htmlStr,
				textDetail: body,
				title: title,
				description:title,
				amount:amount,
				discount:discountAmount,
				code:code,
				type:type,
				imgUrl:url,
				dateofpost:date_,
				starCount: 0,
				authorPic: picture
			  };



			  // Write the new post's data simultaneously in the posts list and the user's post list.
			  var updates = {};
			  updates['/message/items/' + newPostKey] = postData;
			  updates['/user-posts/' + uid + '/' + newPostKey] = postData;

			  firebase.database().ref().update(updates);
  
  
  
        // [START_EXCLUDE]
        document.getElementById('linkbox').innerHTML = '<a href="' +  url + '">Click For File</a>';
        // [END_EXCLUDE]
      }).catch(function(error) {
        // [START onfailure]
        console.error('Upload failed:', error);
		 document.getElementById('linkbox').innerHTML = '<a href="#">File Upload Failed</a>';
        // [END onfailure]
      });
      // [END oncomplete]
	 
	  return true;
	  
	  
	  
	  
 
  
}
// [END write_fan_out]

/**
 * Star/unstar post.
 */
// [START post_stars_transaction]
function toggleStar(postRef, uid) {
  postRef.transaction(function(post) {
    if (post) {
      if (post.stars && post.stars[uid]) {
        post.starCount--;
        post.stars[uid] = null;
      } else {
        post.starCount++;
        if (!post.stars) {
          post.stars = {};
        }
        post.stars[uid] = true;
      }
    }
    return post;
  });
}
// [END post_stars_transaction]

/**
 * Creates a post element.
 */
function createPostElement(postId, title, text, author, authorId, authorPic,imgUrl,code) {
  var uid = firebase.auth().currentUser.uid;

  var html =
      '<div class="post post-' + postId + ' mdl-cell mdl-cell--12-col ' +
                  'mdl-cell--6-col-tablet mdl-cell--4-col-desktop mdl-grid mdl-grid--no-spacing">' +
        '<div class="mdl-card mdl-shadow--2dp">' +
          '<div class="mdl-card__title mdl-color--light-blue-600 mdl-color-text--white">' +
            '<h4 class="mdl-card__title-text"></h4>' +
          '</div>' +
          '<div class="header">' +
            '<div>' +
              '<div class="avatar"></div>' +
              '<div class="username mdl-color-text--black"></div>' +
            '</div>' +
		
          '</div>' +
         
          '<div class="text"></div>' +
			'<div class="mdl-card__actions mdl-card--border"><button type="button"   class="mdl-button mdl-js-button mdl-button--raised mdl-js-ripple-effect">Edit</button></div>'+
          '<div class="comments-container"></div>' +
         
        '</div>' +
      '</div>';

  // Create the DOM element from the HTML.
  var div = document.createElement('div');
  div.innerHTML = html;
  var postElement = div.firstChild;
  /**
  if (componentHandler) {
    componentHandler.upgradeElements(postElement.getElementsByClassName('mdl-textfield')[0]);
  }
  **/
var editButton = postElement.getElementsByClassName('mdl-button')[0];

 // var star = postElement.getElementsByClassName('starred')[0];
 // var unStar = postElement.getElementsByClassName('not-starred')[0];

  // Set values.
  postElement.getElementsByClassName('text')[0].innerText = text;
  postElement.getElementsByClassName('mdl-card__title-text')[0].innerText = '('+ code +')'+title;
  postElement.getElementsByClassName('username')[0].innerText = author || 'Anonymous';
  postElement.getElementsByClassName('avatar')[0].style.backgroundImage = 'url("' +
      (authorPic || './silhouette.jpg') + '")';




  editButton.onclick = function() {
    showSection(updatePost);
	
	 var getSinglePostRef = firebase.database().ref('/message/items/'+postId);
	
	 getSinglePostRef.once('value').then(function(data) {
				 
			
				 titleInputU.value  = data.val().title;
				codeU.value = data.val().code;
				//type.value  = data.val().type;
				var type_=data.val().type;
				var amount0=data.val().amount;
				//desc.value  = data.val().description;
				amountInputU.value  = data.val().amount+"";
				imageUrlU.value  = data.val().imgUrl;
				editTextU.value=data.val().textDetail;
				keyHidden.value=data.key;
				$('#discount-price-edit').val(data.val().discount);
				if(type_=="promo"){
				document.getElementById('option2edit').parentNode.MaterialRadio.check();
					}
				else if(type_=="new"){
					document.getElementById('option3edit').parentNode.MaterialRadio.check();
					}
				else if(type_=="regular"){

					document.getElementById('option1edit').parentNode.MaterialRadio.check();
					
					}
				else if(type_=="unlisted"){

					document.getElementById('option4edit').parentNode.MaterialRadio.check();
					
					}
				$("#editImg").attr('src', data.val().imgUrl );
          
		  });
		  document.getElementById('add').style.display='none';

  };
  return postElement;
}

/**


/**
 * Updates the starred status of the post.
 */
function updateStarredByCurrentUser(postElement, starred) {
  if (starred) {
    postElement.getElementsByClassName('starred')[0].style.display = 'inline-block';
    postElement.getElementsByClassName('not-starred')[0].style.display = 'none';
  } else {
    postElement.getElementsByClassName('starred')[0].style.display = 'none';
    postElement.getElementsByClassName('not-starred')[0].style.display = 'inline-block';
  }
}

/**
 * Updates the number of stars displayed for a post.
 */
function updateStarCount(postElement, nbStart) {
  postElement.getElementsByClassName('star-count')[0].innerText = nbStart;
}





/**
 * Starts listening for new posts and populates posts lists.
 */
function startDatabaseQueries() {
  // [START my_top_posts_query]
  var myUserId = firebase.auth().currentUser.uid;
  var topUserPostsRef = firebase.database().ref('user-posts/' + myUserId).orderByChild('starCount');
  // [END my_top_posts_query]
  // [START recent_posts_query]
  var recentPostsRef = firebase.database().ref('message/items/').limitToLast(100);
  // [END recent_posts_query]
  var userPostsRef = firebase.database().ref('user-posts/' + myUserId);

  var fetchPosts = function(postsRef, sectionElement) {
    postsRef.on('child_added', function(data) {
      var author = data.val().author || 'Anonymous';
      var containerElement = sectionElement.getElementsByClassName('posts-container')[0];
      containerElement.insertBefore(
          createPostElement(data.key, data.val().title, data.val().textDetail, author, data.val().uid, data.val().authorPic,data.val().imgUrl,data.val().code),
          containerElement.firstChild);
    });
    postsRef.on('child_changed', function(data) {	
		var containerElement = sectionElement.getElementsByClassName('posts-container')[0];
		var postElement = containerElement.getElementsByClassName('post-' + data.key)[0];
		postElement.getElementsByClassName('mdl-card__title-text')[0].innerText = data.val().title;
		postElement.getElementsByClassName('username')[0].innerText = data.val().author;
		postElement.getElementsByClassName('text')[0].innerText = data.val().textDetail;
		postElement.getElementsByClassName('star-count')[0].innerText = data.val().starCount;
    });
    postsRef.on('child_removed', function(data) {
		var containerElement = sectionElement.getElementsByClassName('posts-container')[0];
		var post = containerElement.getElementsByClassName('post-' + data.key)[0];
	    post.parentElement.removeChild(post);
    });
  };

  // Fetching and displaying all posts of each sections.
 // fetchPosts(topUserPostsRef, topUserPostsSection);
  fetchPosts(recentPostsRef, recentPostsSection);
  //fetchPosts(userPostsRef, userPostsSection);

  // Keep track of all Firebase refs we are listening to.
 // listeningFirebaseRefs.push(topUserPostsRef);
  listeningFirebaseRefs.push(recentPostsRef);
  //listeningFirebaseRefs.push(userPostsRef);
}

/**
 * Writes the user's data to the database.
 */
// [START basic_write]
function writeUserData(userId, name, email, imageUrl) {
  firebase.database().ref('users/' + userId).set({
    username: name,
    email: email,
    profile_picture : imageUrl
  });
}
// [END basic_write]

/**
 * Cleanups the UI and removes all Firebase listeners.
 */
function cleanupUi() {
  // Remove all previously displayed posts.
  topUserPostsSection.getElementsByClassName('posts-container')[0].innerHTML = '';
  recentPostsSection.getElementsByClassName('posts-container')[0].innerHTML = '';
  userPostsSection.getElementsByClassName('posts-container')[0].innerHTML = '';

  // Stop all currently listening Firebase listeners.
  listeningFirebaseRefs.forEach(function(ref) {
    ref.off();
  });
  listeningFirebaseRefs = [];
}

/**
 * The ID of the currently signed-in User. We keep track of this to detect Auth state change events that are just
 * programmatic token refresh but not a User status change.
 */
var currentUID;

/**
 * Triggers every time there is a change in the Firebase auth state (i.e. user signed-in or user signed out).
 */
function onAuthStateChanged(user) {
  // We ignore token refresh events.
  if (user && currentUID === user.uid) {
    return;
  }

  cleanupUi();
  if (user) {
    currentUID = user.uid;
    splashPage.style.display = 'none';
    writeUserData(user.uid, user.displayName, user.email, user.photoURL);
    startDatabaseQueries();
  } else {
    // Set currentUID to null.
    currentUID = null;
    // Display the splash page where you can sign-in.
    splashPage.style.display = '';
  }
}

/**
 * Creates a new post for the current user.
 */
function newPostForCurrentUser(title, text,desc,amount,code,type,imageUrl) {
  // [START single_value_read]
  
  
  var userId = firebase.auth().currentUser.uid;
  return firebase.database().ref('/users/' + userId).once('value').then(function(snapshot) {
    var username = snapshot.val().username;
    // [START_EXCLUDE]
    return writeNewPost(firebase.auth().currentUser.uid, username,
        firebase.auth().currentUser.photoURL,
        title, text,desc,amount,code,type,imageUrl);
    // [END_EXCLUDE]
  });
  // [END single_value_read]
}

/**
 * Displays the given section element and changes styling of the given button.
 */
function showSection(sectionElement, buttonElement) {
  recentPostsSection.style.display = 'none';
  userPostsSection.style.display = 'none';
  topUserPostsSection.style.display = 'none';
  addPost.style.display = 'none';
   updatePost.style.display = 'none';
  recentMenuButton.classList.remove('is-active');
  myPostsMenuButton.classList.remove('is-active');
  myTopPostsMenuButton.classList.remove('is-active');

  if (sectionElement) {
    sectionElement.style.display = 'block';
  }
  if (buttonElement) {
    buttonElement.classList.add('is-active');
  }
}



function getSequenceNumber()
{
	
	var getConfigRef = firebase.database().ref('/config/code');
	var updateRef = firebase.database().ref('/config/');
	 getConfigRef.once('value').then(function(data) {
				  $("#new-post-code").val("TWP"+data.val());
					 
						var temp=Number(data.val())+1;
						updateRef.update({"code": temp});
		  });
}


function update()
{
	
	var titleu= titleInputU.value;
	var typeu=$('input[name=rtypeedit]:checked').val();
	var amountU=parseFloat(amountInputU.value);
	var textU=editTextU.value;
	var discountU=parseFloat($('#discount-price-edit').val());
	var result=checkDiscount(discountU,typeu);
	if(result==false)
	{return;}
	var key_=keyHidden.value;
			
	  var htmlText=getHtml(textU);
	 firebase.database().ref('message/items/' + key_).update({
						
				htmlDetail: htmlText,
				textDetail: textU,
				title: titleu,
				amount:amountU,
				type:typeu,
				discount:discountU
						
			  });
			  alert("Updated Successfully!");
			   recentMenuButton.click();
	
}


function checkDiscount(discount,type_)
{
	if(type_=="promo" && (discount=="" || discount==0)){
		
		alert("Enter promotion amount");
		return false;
	}
	return true;
	
}

function getHtml(detail)
{
	var temp=detail.replace(/[\n\r]/g, "<br/>"); 
	return format.replace("${body}", temp); 
	
}
// Bindings on load.
window.addEventListener('load', function() {

 updateForm.onsubmit = function(e) {
	 
	 e.preventDefault();
	 update();
 document.getElementById('add').style.display='block';
 };
   
	
  // Bind Sign in button.
  signInButton.addEventListener('click', function() {
    var provider = new firebase.auth.GoogleAuthProvider();
    firebase.auth().signInWithPopup(provider);
  });

  // Bind Sign out button.
  signOutButton.addEventListener('click', function() {
    firebase.auth().signOut();
  });

  // Listen for auth state changes
  firebase.auth().onAuthStateChanged(onAuthStateChanged);

  // Saves message on form submit.
  messageForm.onsubmit = function(e) {
    e.preventDefault();
    var text = messageInput.value;
    var title = titleInput.value;
	var code_=code.value;
	var desc_=titleInput.value;
	var type_=$('input[name=rtype]:checked').val();
	var discount=Number($("#discount-price").val());
	var result=checkDiscount(discount,type_);
	if(result==false)
	{return;}
	var amount_=parseFloat(amount.value);
	var imgUrl=imageUrl.value;
    if (text && title) {
      newPostForCurrentUser(title, text,desc_,amount_,code_,type_,imgUrl).then(function() {
        recentMenuButton.click();
      });
      messageInput.value = '';
      titleInput.value = '';
    }
	else{
	
	}
  };
  


  deleteButton.onclick= function(){
  
  var r = confirm("Are you sure to Delete?");
    if (r == true) {
        var id=keyHidden.value;
		var getSinglePostRef = firebase.database().ref('/message/items/');
		getSinglePostRef.child(id).remove();
		
		
		var str = document.getElementById('edit-post-imageUrl').value;
        var n = str.indexOf("images%2F");
		var m = str.indexOf("?");
		var len="images%2F".length;
		
		// Create a reference to the file to delete
		var desertRef = storageRef.child('images/'+str.substring(n+len,m));

// Delete the file
			desertRef.delete().then(function() {
  // File deleted successfully
          alert('Deleted.');
		    recentMenuButton.onclick();
		}).catch(function(error) {
		  // Uh-oh, an error occurred!
		});


    } else {
	var str = document.getElementById('edit-post-imageUrl').value;
        var n = str.indexOf("images%2F");
		var m = str.indexOf("?");
		var len="images%2F".length;
	
       var txt = "You pressed Cancel!";
    }
    
  };
  // Bind menu buttons.
  recentMenuButton.onclick = function() {
    showSection(recentPostsSection, recentMenuButton);
	 document.getElementById('add').style.display='block';
  };
  myPostsMenuButton.onclick = function() {
    showSection(userPostsSection, myPostsMenuButton);
  };
  myTopPostsMenuButton.onclick = function() {
    showSection(topUserPostsSection, myTopPostsMenuButton);
  };
  addButton.onclick = function() {
    showSection(addPost);
	 updatePost.style.display = 'none';
    messageInput.value = '';
    titleInput.value = '';
	getSequenceNumber();
  };
  recentMenuButton.onclick();
}, false);
