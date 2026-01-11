package sharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.facility_bookuitm.model.User;

public class SharedPrefManager {

        // SharedPreferences name
        private static final String SHARED_PREF_NAME = "facility_shared_pref";

        // Keys
        private static final String KEY_ID = "key_id";
        private static final String KEY_USERNAME = "key_username";
        private static final String KEY_EMAIL = "key_email";
        private static final String KEY_TOKEN = "key_token";
        private static final String KEY_ROLE = "key_role";
        private static final String KEY_IS_ACTIVE = "key_is_active";

        private final Context mCtx;

        // Constructor
        public SharedPrefManager(Context context) {
            this.mCtx = context;
        }

        /**
         * Store logged in user data
         */
        public void storeUser(User user) {
            SharedPreferences sharedPreferences =
                    mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_ID, user.getUserID());
            editor.putString(KEY_USERNAME, user.getUserName());
            editor.putString(KEY_EMAIL, user.getUserEmail());
            editor.putString(KEY_TOKEN, user.getToken());
            editor.putString(KEY_ROLE, user.getUserRole());
            //editor.putInt(KEY_IS_ACTIVE, user.getIs_active());
            editor.apply();
        }

        /**
         * Check whether user already logged in
         */
        public boolean isLoggedIn() {
            SharedPreferences sharedPreferences =
                    mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

            return sharedPreferences.getString(KEY_TOKEN, null) != null;
        }

        /**
         * Get logged in user data
         */
        public User getUser() {
            SharedPreferences sharedPreferences =
                    mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

            User user = new User();
            user.setUserID(sharedPreferences.getString(KEY_ID, null));
            user.setUserName(sharedPreferences.getString(KEY_USERNAME, null));
            user.setUserEmail(sharedPreferences.getString(KEY_EMAIL, null));
            user.setToken(sharedPreferences.getString(KEY_TOKEN, null));
            user.setUserRole(sharedPreferences.getString(KEY_ROLE, null));
            //user.setIs_active(sharedPreferences.getInt(KEY_IS_ACTIVE, 0));

            return user;
        }

        /**
         * Logout user
         */
        public void logout() {
            SharedPreferences sharedPreferences =
                    mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }


