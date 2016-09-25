package com.smile.generous;

import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.httpclient.HttpClient;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import static com.smile.generous.Utils.readUrl;

public class FoodSourceAddEdit extends AppCompatActivity {

    private TimeSlotsAdapter mSlotsAdapter;
    private RecyclerView mSlotsRecyclerView;
    private ImageView addButton;

    private LinearLayout content;
    private ProgressBar progressBar;

    private EditText name;
    private EditText address;
    private EditText phone;
    private EditText description;

    private FoodSource mFoodSource;

    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_source_add_edit);

        mSlotsRecyclerView = (RecyclerView) findViewById(R.id.workingHoursRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mSlotsRecyclerView.setLayoutManager(layoutManager);

        addButton = (ImageView) findViewById(R.id.addButton);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        content = (LinearLayout) findViewById(R.id.content);

        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        description = (EditText) findViewById(R.id.description);

        mId = getIntent().getLongExtra("food_source_id", -1);
        if(mId != -1) {
            Thread thread = new Thread(new Runnable() {
                String jsonData = "";

                @Override
                public void run() {
                    try {
                        jsonData = readUrl("http://139.59.212.15:3045/api/food_source/" + mId + "/?format=json");
                        Gson gson = new Gson();
                        mFoodSource = gson.fromJson(jsonData, FoodSource.class);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FoodSourceAddEdit.this.setTitle("Edit place");
                                progressBar.setVisibility(View.GONE);
                                content.setVisibility(View.VISIBLE);

                                name.append(mFoodSource.getName());
                                description.append(mFoodSource.getDescription());
                                phone.append(mFoodSource.getPhone());
                                address.append(mFoodSource.getAddress());

                                mSlotsAdapter = new TimeSlotsAdapter(mFoodSource.getWorkingHours());
                                mSlotsRecyclerView.setAdapter(mSlotsAdapter);
                                mSlotsAdapter.notifyDataSetChanged();

                                addButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mFoodSource.getWorkingHours().add(new WeeklyFreeFoodSlot());
                                        mSlotsAdapter.notifyItemInserted(mFoodSource.getWorkingHours().size() - 1);
                                    }
                                });
                            }
                        });
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(FoodSourceAddEdit.this, "There was some connection problem. " +
                                        "Please try again later",
                                Toast.LENGTH_LONG).show();
                        FoodSourceAddEdit.this.finish();
                    }
                }
            });

            thread.start();
        } else {
            FoodSourceAddEdit.this.setTitle("Add new place");
            progressBar.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);

            mFoodSource = new FoodSource();

            mSlotsAdapter = new TimeSlotsAdapter(mFoodSource.getWorkingHours());
            mSlotsRecyclerView.setAdapter(mSlotsAdapter);
            mSlotsAdapter.notifyDataSetChanged();

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFoodSource.getWorkingHours().add(new WeeklyFreeFoodSlot());
                    mSlotsAdapter.notifyItemInserted(mFoodSource.getWorkingHours().size() - 1);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            super.onBackPressed();
            return true;
        } else if (id == R.id.save_place) {
            boolean fieldValidationPassed = true;

            mFoodSource.setName(name.getText().toString());
            mFoodSource.setAddress(address.getText().toString());
            mFoodSource.setPhone(phone.getText().toString());
            mFoodSource.setDescription(description.getText().toString());



            /*if(sName.isEmpty()) {
                fieldValidationPassed = false;
                edt_nome_materia.setError(this.getResources().getString(R.string.inserisci_un_nome));
            }

            else if(mSelectedColorPosition == -1) {
                Random r = new Random();
                mSelectedColorPosition = r.nextInt(mSubjectsColors.length);
            } */

            if(fieldValidationPassed) {
                if(mId == -1) {
                    // TODO POST without id

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpClient httpClient = new HttpClient(); //Deprecated

                            try {
                                Gson gson = new Gson();
                                String json = gson.toJson(mFoodSource);

                                URLConnection connection = new URL("http://139.59.212.15:3045/api/food_source/").openConnection();
                                connection.setDoOutput(true); // Triggers POST.
                                connection.setRequestProperty("Content-Type", "application/json");

                                try (OutputStream output = connection.getOutputStream()) {
                                    output.write(URLEncoder.encode(json, "UTF-8").getBytes("UTF-8"));
                                }

                                InputStream response = connection.getInputStream();
                                // handle response here...
                            }

                            catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    thread.start();


                } else {
                    // TODO POST with old id
                }

                setResult(RESULT_OK, null);
                super.onBackPressed();
            }

            else {
                Toast.makeText(FoodSourceAddEdit.this, "Some fields are not filled correctly. " +
                        "Please double-check", Toast.LENGTH_SHORT).show();
            }
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
            public TextView startTime;
            public TextView endTime;
            public ImageView removeButton;

            public ViewHolder(View itemView) {
                super(itemView);

                day = (TextView) itemView.findViewById(R.id.weekday);
                startTime = (TextView) itemView.findViewById(R.id.startHour);
                endTime = (TextView) itemView.findViewById(R.id.endHour);
                removeButton = (ImageView) itemView.findViewById(R.id.removeButton);
            }
        }

        @Override
        public void onBindViewHolder(final TimeSlotsAdapter.ViewHolder viewHolder, final int position) {
            final WeeklyFreeFoodSlot c = mWeeklyFreeFoodSlots.get(position);
            viewHolder.day.setText(c.getDay());

            viewHolder.day.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mFoodSource.getWorkingHours().get(viewHolder.getAdapterPosition()).setDay(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });

            refreshTime(c, viewHolder);

            viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWeeklyFreeFoodSlots.remove(c);
                    notifyItemRemoved(position);
                }
            });

            viewHolder.startTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar mcurrentTime = Calendar.getInstance();

                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);

                    TimePickerDialog mTimePicker;

                    mTimePicker = new TimePickerDialog(FoodSourceAddEdit.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            c.setStartTime(3600*selectedHour + 60*selectedMinute);
                            refreshTime(c, viewHolder);
                        }
                    }, hour, minute, true);//Yes 24 hour time

                    mTimePicker.setTitle("");
                    mTimePicker.show();
                }
            });

            viewHolder.endTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar mcurrentTime = Calendar.getInstance();

                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);

                    TimePickerDialog mTimePicker;

                    mTimePicker = new TimePickerDialog(FoodSourceAddEdit.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            c.setEndTime(3600*selectedHour + 60*selectedMinute);
                            refreshTime(c, viewHolder);
                        }
                    }, hour, minute, true);//Yes 24 hour time

                    mTimePicker.setTitle("");
                    mTimePicker.show();
                }
            });
        }

        private void refreshTime(WeeklyFreeFoodSlot c, ViewHolder viewHolder) {
            int hourStart = c.getStartTime()/3600;
            int minuteStart = (c.getStartTime()%3600)/60;

            int hourEnd = c.getEndTime()/3600;
            int minuteEnd = (c.getEndTime()%3600)/60;

            DecimalFormat df = new DecimalFormat("00");

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

            String start = df.format(hourStart) + ":" + df.format(minuteStart) +
                    (pmHourStart ? "pm" : "am");
            viewHolder.startTime.setText(start);

            String end = df.format(hourEnd) + ":" +
                    df.format(minuteEnd) + (pmHourEnd ? "pm" : "am");
            viewHolder.endTime.setText(end);
        }

        @Override
        public int getItemCount() {
            return mWeeklyFreeFoodSlots == null ? 0 : mWeeklyFreeFoodSlots.size();
        }
    }
}
