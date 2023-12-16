# Food Delivery App - Android Application

Welcome to the Food Delivery App project for the CSCI-C323 Mobile App Development course. This Android app allows users to order food items from various restaurants, providing a seamless experience from sign-up to order placement and tracking. The app is built using Kotlin and utilizes Firebase for authentication, Firestore for database management, and Cloud Storage for image storage.

## Project Overview

### Sign-up Screen
- Users can sign up with their email and name.
- Image upload functionality allows users to set a profile picture.

### Navigation Drawer and Home Screen
- Displays user information in the navigation drawer.
- Options for Home, Recent Orders, and Sign-out in the navigation drawer.
- Home screen includes a search icon for finding restaurants.
- Recent restaurants and all restaurants are displayed in horizontally and vertically scrollable fragments, respectively.

### Restaurant Screen
- Shows restaurant/food images in a swipeable fragment.
- Lists all food items with add and delete functionality.
- Tracks the quantity of each item.
- Checkout button for order processing.

### Checkout Screen
- Displays added food items with details.
- Allows users to modify the order and enter delivery address and special instructions.
- Calculates delivery time and schedules a notification.
- Place Order button stores order details in the database.

### Orders Screen
- Lists all past orders with details.
- Track Order button opens the Map screen to display order location.

### Map Screen
- Displays order location on a map with route highlighting.

### Recent Orders Screen
- Lists all past orders with an option to place the same order again.

### Calendar View Screen
- Displays a calendar with highlighted spending information for each day.



## Bonus Feature (10%)
Bonus implementation of the Calender is present


## Video Walkthrough



## Notes

The app's logic is fully implemented and functional. However, there might be a minor issue causing it not to compile.

## License

Copyright 2023 by [George Sackie]

Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0).

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
