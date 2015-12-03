package com.nothing.mark.popmoviesapp.Sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import timber.log.Timber;

import com.nothing.mark.popmoviesapp.R;
import com.nothing.mark.popmoviesapp.Database.MoviesContract;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public SyncAdapter(Context context, boolean bundle) {
        super(context, bundle);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String authority,
                              ContentProviderClient provider, SyncResult result) {
        Timber.d("SyncAdapter.onPerformSync");
    }

    public static void syncImmediately(Context context) {
        Timber.d("SyncAdapter.syncImmediately");

        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        
        ContentResolver.requestSync(getSyncAccount(context), MoviesContract.CONTENT_AUTHORITY, bundle);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account newAccount = new Account(context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if (null == accountManager.getPassword(newAccount) && !accountManager.addAccountExplicitly(newAccount, "", null))
                return null;

        return newAccount;
    }
}