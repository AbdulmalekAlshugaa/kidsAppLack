package com.example.user.kidsapplocksystem;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kidsapplocksystem.localdb.AppModel;
import com.example.user.kidsapplocksystem.localdb.AppModelDao;
import com.example.user.kidsapplocksystem.localdb.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zack on 05/03/2017.
 */

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    private List<AppModel> mApps = new ArrayList<>();
    private Manageapps mMainActivity;
    private LayoutInflater layoutInflater;
    private AppAdapterListener mAppsAdapterListener;

    public AppAdapter(Manageapps activity) {
        mMainActivity = activity;
        layoutInflater = mMainActivity.getLayoutInflater();
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_app, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        AppModel app = mApps.get(position);
        holder.imageAppIcon.setImageDrawable(app.getIcon());
        holder.textAppName.setText(app.getAppName());
        holder.switchBlockApp.setChecked(isBlocked(app));
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    private boolean isBlocked(AppModel app) {
        AppModelDao appModelDao = getAppDaoSession().getAppModelDao();
        QueryBuilder<AppModel> queryBuilder =
                appModelDao.queryBuilder().where(AppModelDao.Properties.PackageName.eq(app.getPackageName()));
        if (queryBuilder.list().size() > 0) {
            AppModel appModel = queryBuilder.list().get(0);
            if (appModel.getIsPermanent()) {
                return true;
            }
            if (appModel.getStartBlockTime() + appModel.getBlockDuration() > System.currentTimeMillis()) {
                return true;
            } else {
                appModelDao.delete(appModel);
            }
            return false;
        } else {
            return false;
        }
    }

    public void setApps(List<AppModel> apps) {
        mApps = apps;
        notifyDataSetChanged();
    }

    public void setAppsAdapterListener(AppAdapterListener appsAdapterListener) {
        this.mAppsAdapterListener = appsAdapterListener;
    }

    private DaoSession getAppDaoSession() {
        return ((MyApplication) mMainActivity.getApplication()).getDaoSession();
    }


    public interface AppAdapterListener {

        void onBlockApp(AppModel app, int index);

        void onTemporaryBlockApp(AppModel appModel);
    }

    class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAppIcon;
        TextView textAppName;
        Switch switchBlockApp;

        public AppViewHolder(View itemView) {
            super(itemView);
            imageAppIcon = (ImageView) itemView.findViewById(R.id.image_app_icon);
            textAppName = (TextView) itemView.findViewById(R.id.text_app_name);
            switchBlockApp = (Switch) itemView.findViewById(R.id.switch_lock);
            switchBlockApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (switchBlockApp.isShown()) {
                        if (isChecked) {
                            showSelectionDialog();
                        } else {
                            AppModelDao appModelDao = getAppDaoSession().getAppModelDao();
                            QueryBuilder<AppModel> appModel =
                                    appModelDao.queryBuilder().where(AppModelDao.Properties.PackageName.
                                            eq(mApps.get(getAdapterPosition()).getPackageName()));
                            if (appModel.list().size() > 0) {
                                AppModel app = appModel.list().get(0);
                                appModelDao.delete(app);
                            }
                        }
                    }
                }
            });
        }

        private void showTimeDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
            final View dialogView = layoutInflater.inflate(R.layout.dialog_duration, null);

            final EditText editTextDuration =
                    (EditText) dialogView.findViewById(R.id.edit_text_duration);
            builder.setView(dialogView);

            builder.setTitle("Block duration")
                    .setMessage("Enter duration in minutes below")
                    .setCancelable(false)
                    .setPositiveButton("Block", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String durationString = editTextDuration.getText().toString();
                            if (!TextUtils.isEmpty(durationString)) {
                                long duration = Long.parseLong(durationString);
                                long durationMS = duration * 60 * 1000;
                                AppModel appModel = mApps.get(getAdapterPosition());
                                appModel.setPermanent(false);
                                appModel.setStartBlockTime(System.currentTimeMillis());
                                appModel.setBlockDuration(durationMS);
                                getAppDaoSession().getAppModelDao().insert(appModel);
                                Toast.makeText(mMainActivity, appModel.getAppName() + " has been blocked for " +
                                        durationString + " minute", Toast.LENGTH_SHORT).show();
                                if (mAppsAdapterListener != null) {
                                    mAppsAdapterListener.onTemporaryBlockApp(appModel);
                                }
                            } else {
                                Toast.makeText(mMainActivity, "duration cannot be empty",
                                        Toast.LENGTH_SHORT).show();
                                notifyItemChanged(getAdapterPosition());
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mMainActivity, "Block canceled",
                                    Toast.LENGTH_SHORT).show();
                            notifyItemChanged(getAdapterPosition());
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        private void showSelectionDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mMainActivity);
            builder.setTitle("Block Type")
                    .setCancelable(false)
                    .setItems(new CharSequence[]{"Permanent", "Temporary"}, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                AppModel appModel = mApps.get(getAdapterPosition());
                                appModel.setPermanent(true);
                                appModel.setStartBlockTime(System.currentTimeMillis());
                                getAppDaoSession().getAppModelDao().insert(appModel);
                                Toast.makeText(mMainActivity, appModel.getAppName() + "has been blocked",
                                        Toast.LENGTH_SHORT).show();
                            } else if (which == 1) {
                                showTimeDialog();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mMainActivity, "Block canceled",
                                    Toast.LENGTH_SHORT).show();
                            notifyItemChanged(getAdapterPosition());
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
