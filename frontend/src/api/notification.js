import firebase from 'firebase/app';
import 'firebase/messaging';

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: 'AIzaSyD9OWUMtBRfh_tVjp55WliCUeSl-TSK5Tc',
  authDomain: 'myownmap-304803.firebaseapp.com',
  projectId: 'myownmap-304803',
  storageBucket: 'myownmap-304803.appspot.com',
  messagingSenderId: '478942327447',
  appId: '1:478942327447:web:fb8f36cafd574b60366fd2',
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

function getMessagingObject() {
  // [START messaging_get_messaging_object]
  // const messaging = firebase.messaging();
  return messaging;
  // [END messaging_get_messaging_object]
}

// function receiveMessage() {
//   // const messaging = firebase.messaging();
//   // [START messaging_receive_message]
//   // Handle incoming messages. Called when:
//   // - a message is received while the app has focus
//   // - the user clicks on an app notification created by a service worker
//   //   `messaging.onBackgroundMessage` handler.
//   messaging.onMessage((payload) => {
//     console.log('Message received. ', payload);

//     let title = payload.notification.title;
//     let options = {
//       body: payload.notification.body,
//     };

//     try {
//       new Notification(title, options);
//     } catch (e) {
//       console.log(e);
//     }

//     // ...
//   });
//   // [END messaging_receive_message]
// }

function getToken(success) {
  // const messaging = firebase.messaging();
  // [START messaging_get_token]
  // Get registration token. Initially this makes a network call, once retrieved
  // subsequent calls to getToken will return from cache.
  messaging
    .getToken({
      vapidKey:
        'BD0FizUEkQ7pxY_B_rYqKwmnfPUbs2XeWbL2wwax3WOA4MWig5VUAByXYTgZnph2oBDq-E76NYMj3X4sylyS7gU',
    })
    .then(success)
    .catch((err) => {
      console.log('An error occurred while retrieving token. ', err);
      // ...
    });
  // [END messaging_get_token]
}

function requestPermission(permission) {
  // [START messaging_request_permission]
  Notification.requestPermission().then(permission);
  // [END messaging_request_permission]
}

function deleteToken(success) {
  // const messaging = firebase.messaging();

  // [START messaging_delete_token]
  messaging
    .deleteToken()
    .then(success)
    .catch((err) => {
      console.log('Unable to delete token. ', err);
    });
  // [END messaging_delete_token]
}

export {
  getMessagingObject,
  // receiveMessage,
  getToken,
  requestPermission,
  deleteToken,
};
