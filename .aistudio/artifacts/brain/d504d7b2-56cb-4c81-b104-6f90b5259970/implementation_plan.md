# Real-time Multi-Device Sync for CGPI Class Routine

This plan outlines how to enable cloud synchronization so that when the Admin updates a class routine, announcement notice, or teacher contact on one phone, the changes instantly reflect across **all students' mobile devices** where the app is installed.

---

## User Review & Critical Decisions

> [!IMPORTANT]
> **Current Status vs. Cloud Sync**:
> - **Currently**: Routine changes and notices made on one device are saved locally on that specific phone. Other phones will not see the changes.
> - **With Cloud Sync (Firebase Firestore)**: When the Admin changes a routine period, room number, notice, or teacher phone number on their device, it updates in the cloud and automatically syncs to **every student's phone in real time**.
> - **Offline Capability**: When students are offline (no internet), the app continues to display the latest cached schedule and sound local class alarms. Once connected to mobile data or Wi-Fi, it seamlessly fetches updates.

---

## 1. Overview & Core Concept

- **What It Delivers**: Real-time broadcast and synchronization of timetable changes, room shifts, teacher contacts, and emergency notices from the Admin device to all student devices across Chattogram Government Polytechnic Institute.
- **Target Audience**: 1st Semester students and faculty of Electrical Technology (First Shift - A1 & B1).
- **Security & Authorization**: The Admin updates are authenticated and restricted via PIN (`272426`) or Admin privileges, while regular students receive read-only real-time updates.

---

## 2. User Experience & Flows

### Admin Flow (Single Device Modification)
1. Admin logs into the **Admin Control Panel** using PIN `272426`.
2. Admin modifies a class (e.g. changes Physics room to Room 325, cancels a period, or posts a new exam announcement).
3. Admin taps **Save**. Changes are immediately saved locally and synced to Firebase Firestore in the background.

### Student Flow (All Connected Devices)
1. Student opens the app on their phone.
2. A lightweight snapshot listener detects any new updates from the cloud in real time.
3. The timetable, room number, teacher phone, or announcement notice refreshes automatically without requiring an app reinstall or update.
4. Next-class countdown and local alarm notifications automatically reschedule based on the new timing or room.

---

## 3. Technical Architecture & Data Strategy

```
┌────────────────────────────────────────────────────────┐
│                   Admin Phone                          │
│  [Admin Panel] ─── PIN: 272426 ───> [AdminManager]     │
└──────────────────────────┬─────────────────────────────┘
                           │ Writes Updates
                           ▼
┌────────────────────────────────────────────────────────┐
│             Firebase Cloud Firestore                   │
│  Collection: `routine_updates`                         │
│  Collection: `notices`                                 │
│  Collection: `faculty_directory`                       │
└────────────┬─────────────────────────────┬─────────────┘
             │ Real-time Snapshot Sync     │ Real-time Snapshot Sync
             ▼                             ▼
┌───────────────────────────┐ ┌──────────────────────────┐
│      Student Phone A      │ │     Student Phone B      │
│  [Local Cache + Alarms]   │ │  [Local Cache + Alarms]  │
│  Live Schedule Refreshes  │ │  Live Schedule Refreshes │
└───────────────────────────┘ └──────────────────────────┘
```

### Data Collections in Firestore:
1. `routine_updates/{section_day_period}`: Overrides for specific periods (subject, room, teacher, isCancelled).
2. `notices/current_announcement`: Broadcast text and active status displayed on student home screens.
3. `faculty/directory`: Up-to-date phone numbers and instructors for each department subject.

---

## 4. Execution Phases

- **Phase 1: Firebase Setup**: Provision Firestore using the platform's setup tools with zero-trust security rules (public read for students, authenticated write for admin).
- **Phase 2: Repository Realtime Listener**: Add Firestore snapshot listeners in `RoutineRepository` to merge cloud updates with default schedule data.
- **Phase 3: Admin Cloud Publishing**: Connect the existing Admin Panel save actions to publish updates directly to Firestore.
- **Phase 4: Automatic Local Alarm Rescheduling**: When a cloud update changes class timing or room, re-run `NotificationHelper.scheduleWeeklyReminders` to keep alarms accurate.
