
// // Start writing functions
// // https://firebase.google.com/docs/functions/typescript
//
// export const helloWorld = functions.https.onRequest((request, response) => {
//   functions.logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });
import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

admin.initializeApp();

export const sendOrderStateNotification = functions.firestore
  .document("Orders/{orderId}")
  .onUpdate(async (change, context) => {
    const beforeState = change.before.data()?.orderState;
    const afterState = change.after.data()?.orderState;

    // Check if the order state was actually updated
    if (beforeState === afterState) {
      return null;
    }

    const token = change.after.data()?.token;
    const message = {
      notification: {
        title: "Order Status Update",
        body: `Your order is now ${afterState}`,
      },
      token: token,
    };

    try {
      // Send the notification via Firebase Cloud Messaging
      await admin.messaging().send(message);
      console.log("Notification sent successfully:", message);
      return null;
    } catch (err) {
      console.error("Error sending notification:", err);
      return null;
    }
  });
