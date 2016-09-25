package com.smile.hungry;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

import static com.smile.hungry.Utils.readUrl;

public class FoodSourceDetails extends AppCompatActivity {

    private TimeSlotsAdapter mSlotsAdapter;
    private RecyclerView mSlotsRecyclerView;
    private ProgressBar mProgressBar;
    private LinearLayout mDescriptionLayout;
    private LinearLayout mDaysOfFreeTitle;

    private TextView mDescription;
    private TextView mAddress;
    private TextView mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_source_details);

        mSlotsRecyclerView = (RecyclerView) findViewById(R.id.freeFoodSlotsRecyclerView);
        mDescriptionLayout = (LinearLayout) findViewById(R.id.descriptionLayout);
        mDaysOfFreeTitle = (LinearLayout) findViewById(R.id.daysOfFreeTitle);

        mDescription = (TextView) findViewById(R.id.description);
        mAddress = (TextView) findViewById(R.id.address);
        mPhone = (TextView) findViewById(R.id.phone);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSlotsRecyclerView.setLayoutManager(layoutManager);

        final long id = getIntent().getLongExtra("food_source_id", 1);

        Thread thread = new Thread(new Runnable() {
            String jsonData = "";

            @Override
            public void run() {
                try {
                    jsonData = readUrl("http://139.59.212.15:3045/api/food_source/" + id + "/?format=json");
                    Gson gson = new Gson();
                    final FoodSource foodSource = gson.fromJson(jsonData, FoodSource.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            mSlotsAdapter = new TimeSlotsAdapter(foodSource.getWorkingHours());

                            mDescriptionLayout.setVisibility(View.VISIBLE);
                            mDaysOfFreeTitle.setVisibility(View.VISIBLE);

                            mDescription.setText(foodSource.getDescription());
                            mPhone.setText(foodSource.getPhone());
                            mAddress.setText(foodSource.getAddress());

                            mSlotsRecyclerView.setAdapter(mSlotsAdapter);
                            mSlotsAdapter.notifyDataSetChanged();

                            FoodSourceDetails.this.setTitle(foodSource.getName());
                        }
                    });
                }

                catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FoodSourceDetails.this, "There was some connection problem. " +
                                    "Please try again later",
                            Toast.LENGTH_LONG).show();
                    FoodSourceDetails.this.finish();
                }
            }
        });

        thread.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // We don't want the parent activity to be recreated
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.ViewHolder> {
        private List<WeeklyFreeFoodSlot> mWeeklyFreeFoodSlots;

        public TimeSlotsAdapter(List<WeeklyFreeFoodSlot> tasks) {
            this.mWeeklyFreeFoodSlots = tasks;
        }

        @Override
        public TimeSlotsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            View newView = inflater.inflate(R.layout.item_in_slot, parent, false);
            return new ViewHolder(newView);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView day;
            public TextView time;

            public ViewHolder(View itemView) {
                super(itemView);

                day = (TextView) itemView.findViewById(R.id.day);
                time = (TextView) itemView.findViewById(R.id.time);
            }
        }

        @Override
        public void onBindViewHolder(final TimeSlotsAdapter.ViewHolder viewHolder, int position) {
            WeeklyFreeFoodSlot c = mWeeklyFreeFoodSlots.get(position);
            viewHolder.day.setText(c.getDay());

            int hourStart = c.getStartTime()/3600;
            int minuteStart = (c.getStartTime()%3600)/60;

            int hourEnd = c.getEndTime()/3600;
            int minuteEnd = (c.getEndTime()%3600)/60;

            DecimalFormat df = new DecimalFormat("00");
            String angleFormated = df.format(hourStart);

            boolean pmHourStart = false;
            if(hourStart > 12) {
                pmHourStart = true;
                hourStart -= 12;
            } else if (hourStart == 12) {
                pmHourStart = true;
            }

            boolean pmHourEnd = false;
            if(hourEnd > 12) {
                pmHourEnd = true;
                hourEnd -= 12;
            } else if (hourEnd == 12) {
                pmHourEnd = true;
            }

            String time = df.format(hourStart) + ":" + df.format(minuteStart) +
                    (pmHourStart ? "pm" : "am") + " - " + df.format(hourEnd) + ":" +
                    df.format(minuteEnd) + (pmHourEnd ? "pm" : "am");

            viewHolder.time.setText(time);
        }

        @Override
        public int getItemCount() {
            return mWeeklyFreeFoodSlots == null ? 0 : mWeeklyFreeFoodSlots.size();
        }
    }
}
