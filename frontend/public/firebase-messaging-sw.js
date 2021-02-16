// Give the service worker access to Firebase Messaging.
// Note that you can only use Firebase Messaging here. Other Firebase libraries
// are not available in the service worker.
importScripts('https://www.gstatic.com/firebasejs/8.2.3/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/8.2.3/firebase-messaging.js');

// Initialize the Firebase app in the service worker by passing in
// your app's Firebase config object.
// https://firebase.google.com/docs/web/setup#config-object
// Your web app's Firebase configuration
var firebaseConfig = {
  apiKey: 'AIzaSyD9OWUMtBRfh_tVjp55WliCUeSl-TSK5Tc',
  authDomain: 'myownmap-304803.firebaseapp.com',
  projectId: 'myownmap-304803',
  storageBucket: 'myownmap-304803.appspot.com',
  messagingSenderId: '478942327447',
  appId: '1:478942327447:web:fb8f36cafd574b60366fd2',
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);

// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
const messaging = firebase.messaging();

messaging.setBackgroundMessageHandler((payload) => {
  const title = payload.notification.title;
  const options = {
    body: payload.notification.body,
  };

  return self.registration.showNotification(title, options);
});

messaging.onMessage((payload) => {
  console.log('Message received. ', payload);

  let title = payload.notification.title;
  let options = {
    body: payload.notification.body,
  };

  return self.registration.showNotification(title, options);
});
