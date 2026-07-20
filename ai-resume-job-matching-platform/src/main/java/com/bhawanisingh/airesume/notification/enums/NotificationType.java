package com.bhawanisingh.airesume.notification.enums;

public enum NotificationType {

    // --- APPLICATION RELATED (For Candidate & Recruiter) ---
    APPLICATION_SUBMITTED,       // Candidate ne apply kiya
    APPLICATION_STATUS_UPDATED,  // Application ka status change hua
    APPLICATION_REVIEWED,        // Recruiter ne resume dekha
    APPLICATION_SHORTLISTED,     // Candidate shortlist ho gaya (Accept)
    APPLICATION_REJECTED,        // Candidate reject ho gaya
    APPLICATION_HIRED,           // Candidate hire ho gaya
    NEW_APPLICATION_RECEIVED,    // Recruiter ko alert jab koi nayi application aaye

    // --- RESUME & AI RELATED (For Candidate) ---
    RESUME_PARSED,               // Resume successfully parse ho gaya
    RESUME_PARSE_FAILED,         // Resume parsing mein error aayi
    RESUME_AI_FEEDBACK_READY,    // AI ne resume ka feedback aur score generate kar diya

    // --- JOB RELATED (For Candidate & Recruiter) ---
    NEW_JOB_POSTED,              // (Candidate ke liye) Nayi job system mein aayi
    JOB_MATCH_FOUND,             // (Candidate ke liye) AI ne unke skills ke hisaab se best job dhoondhi
    JOB_POSTED_SUCCESSFULLY,     // (Recruiter ke liye) Job successfully live ho gayi
    JOB_EXPIRED,                 // (Recruiter ke liye) Job ki deadline khatam ho gayi
    JOB_CLOSED,                  // (Recruiter ke liye) Job manually close kar di gayi

    // --- ACCOUNT & SYSTEM (General) ---
    WELCOME_MESSAGE,             // Naya account banne par welcome alert
    SYSTEM_ALERT,                // System maintenance ya general info
    SECURITY_ALERT               // Password change ya login aisi cheezon ke liye
}