package rj.bkinfotech.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import rj.bkinfotech.TaskCompleted;

public class DateAsyncTaskApp extends AsyncTask<Void, Void, String[]> {
    private String array_of_month[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private String array_of_day_code[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private HashMap<String, Integer> days_associated_code;
    private String appointment_dates[];
    private int current_day_int;
    private int current_day_string_to_hash_map;
    private int current_month;
    private TaskCompleted mCallback;


    public DateAsyncTaskApp(Context context) {
        this.mCallback = (TaskCompleted) context;

        days_associated_code = new HashMap<>();
        days_associated_code.put("Sun", 1);
        days_associated_code.put("Mon", 2);
        days_associated_code.put("Tue", 3);
        days_associated_code.put("Wed", 4);
        days_associated_code.put("Thu", 5);
        days_associated_code.put("Fri", 6);
        days_associated_code.put("Sat", 7);


        appointment_dates = new String[6];
        appointment_dates[0] = "Select Allotment Date";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("Inside provid", " pre execute provide_appointment_dates");
        int current_time_int = Integer.parseInt(new SimpleDateFormat("HH", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
        current_day_int = Integer.parseInt(new SimpleDateFormat("dd", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
        current_month = Integer.parseInt(new SimpleDateFormat("MM", Locale.ENGLISH).format(Calendar.getInstance().getTime()));
        String current_day_string = new SimpleDateFormat("E", Locale.ENGLISH).format(Calendar.getInstance().getTime());
        current_day_string_to_hash_map = days_associated_code.get(current_day_string);

            /*Log.d("current_time_int", String.valueOf(current_time_int));
            Log.d("current_time_int", String.valueOf(current_day_int));
            Log.d("current_time_int", String.valueOf(current_month));
            Log.d("current_time_int", String.valueOf(current_day_string));*/

            /*
            current_time_int = 13;
            current_day_int=  13;


//include current_day_string_to_hash_map code to set it back to initial value
            current_month= 6;
            current_day_string = "Wed";
            current_day_string_to_hash_map = days_associated_code.get(current_day_string);//7
            //Log.d("local_current_day 1",String.valueOf(current_day_string_to_hash_map));
            Log.d("current_time_int", String.valueOf(current_time_int));
            Log.d("current_time_int", String.valueOf(current_day_int));
            Log.d("current_time_int", String.valueOf(current_month));
            Log.d("current_time_int", String.valueOf(current_day_string));*/

    }


    @Override
    protected String[] doInBackground(Void... params) {
        return provide_appointment_dates(current_day_int, current_month, current_day_string_to_hash_map);
    }

    @Override
    protected void onPostExecute(String[] appointedDates) {
        try {
            super.onPostExecute(appointedDates);
            mCallback.onTaskComplete(appointedDates);
            Log.d("Inside provid", " POST execute provide_appointment_dates");
        } catch (Exception e) {
            Log.e("DateAsyncTask", e.toString());
            e.printStackTrace();
        }
    }


    private String[] provide_appointment_dates(int local_current_day_int, int local_current_month, int local_current_day_string_to_hash_map) {
        Log.d("Inside provid", " Inside provide_appointment_dates");
        /*local_current_day_int++;
        if(local_current_day_string_to_hash_map==7)
        {
            local_current_day_string_to_hash_map=1;
        }else{
            local_current_day_string_to_hash_map++;
        }*/

        //Log.d("local_current_day 2",String.valueOf(local_current_day_string_to_hash_map));
        int count_dates = 1;
        Log.d("Inside  provid", " Inside inside case 2 provide_appointment_dates");
        switch (local_current_month) {

            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:

                Log.d("Inside  provid", " Inside inside provide_appointment_dates");
                while (count_dates < 6) {
                    if (local_current_day_int >= 31) {
                        if (local_current_day_int == 31) {
                            if (local_current_day_string_to_hash_map != 1) {
                                appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                                count_dates++;
                                local_current_day_string_to_hash_map++;
                            } else {
                                local_current_day_string_to_hash_map++;
                            }
                            local_current_month++;
                            local_current_day_int = 1;
                        } else {
                            local_current_month++;
                            local_current_day_int = 1;
                            if (local_current_day_string_to_hash_map != 1) {
                                appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                                local_current_day_int++;
                                local_current_day_string_to_hash_map++;
                                count_dates++;
                            } else {
                                local_current_day_string_to_hash_map++;
                                local_current_day_int++;
                            }
                        }
                    } else {
                        if (local_current_day_string_to_hash_map != 1) {
                            appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                            local_current_day_int++;
                            count_dates++;
                            local_current_day_string_to_hash_map++;
                        } else {
                            local_current_day_int++;
                            local_current_day_string_to_hash_map++;
                        }
                    }
                    if (local_current_day_string_to_hash_map == 8) {
                        local_current_day_string_to_hash_map = 1;
                    }
                }


                break;
            case 2:
                Log.d("Inside  provid", " Inside inside provide_appointment_dates");
                while (count_dates < 6) {
                    if (local_current_day_int >= 28) {
                        if (local_current_day_int == 28) {
                            if (local_current_day_string_to_hash_map != 1) {
                                appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                                count_dates++;
                                local_current_day_string_to_hash_map++;
                            } else {
                                local_current_day_string_to_hash_map++;
                            }
                            local_current_month++;
                            local_current_day_int = 1;
                        } else {
                            local_current_month++;
                            local_current_day_int = 1;
                            if (local_current_day_string_to_hash_map != 1) {
                                appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                                local_current_day_int++;
                                local_current_day_string_to_hash_map++;
                                count_dates++;
                            } else {
                                local_current_day_string_to_hash_map++;
                                local_current_day_int++;
                            }
                        }
                    } else {
                        if (local_current_day_string_to_hash_map != 1) {
                            appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                            local_current_day_int++;
                            count_dates++;
                            local_current_day_string_to_hash_map++;
                        } else {
                            local_current_day_int++;
                            local_current_day_string_to_hash_map++;
                        }
                    }

                    if (local_current_day_string_to_hash_map == 8) {
                        local_current_day_string_to_hash_map = 1;
                    }
                }

                break;
            case 4:
            case 6:
            case 9:
            case 11:
                Log.d("Inside  provid", " Inside inside provide_appointment_dates");
                while (count_dates < 6) {
                    if (local_current_day_int >= 30) {
                        if (local_current_day_int == 30) {
                            if (local_current_day_string_to_hash_map != 1) {
                                appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                                count_dates++;
                                local_current_day_string_to_hash_map++;
                            } else {
                                local_current_day_string_to_hash_map++;
                            }
                            local_current_month++;
                            local_current_day_int = 1;
                        } else {
                            local_current_month++;
                            local_current_day_int = 1;
                            if (local_current_day_string_to_hash_map != 1) {
                                appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                                local_current_day_int++;
                                local_current_day_string_to_hash_map++;
                                count_dates++;
                            } else {
                                local_current_day_string_to_hash_map++;
                                local_current_day_int++;
                            }
                        }
                    } else {
                        if (local_current_day_string_to_hash_map != 1) {
                            appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                            local_current_day_int++;
                            count_dates++;
                            local_current_day_string_to_hash_map++;
                        } else {
                            local_current_day_int++;
                            local_current_day_string_to_hash_map++;
                        }
                    }
                    if (local_current_day_string_to_hash_map == 8) {
                        local_current_day_string_to_hash_map = 1;
                    }
                }

                break;
            case 12:
                Log.d("Inside  provid", " Inside inside provide_appointment_dates");
                while (count_dates < 6) {
                    if (local_current_day_int >= 31) {
                        if (local_current_day_int == 31) {
                            if (local_current_day_string_to_hash_map != 1) {
                                appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                                count_dates++;
                                local_current_day_string_to_hash_map++;
                            } else {
                                local_current_day_string_to_hash_map++;
                            }
                            local_current_month = 1;
                            local_current_day_int = 1;
                        } else {
                            local_current_month++;
                            local_current_day_int = 1;
                            if (local_current_day_string_to_hash_map != 1) {
                                appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                                local_current_day_int++;
                                local_current_day_string_to_hash_map++;
                                count_dates++;
                            } else {
                                local_current_day_string_to_hash_map++;
                                local_current_day_int++;
                            }
                        }
                    } else {
                        if (local_current_day_string_to_hash_map != 1) {
                            appointment_dates[count_dates] = local_current_day_int + " " + array_of_month[local_current_month - 1] + " " + array_of_day_code[local_current_day_string_to_hash_map - 1];
                            local_current_day_int++;
                            count_dates++;
                            local_current_day_string_to_hash_map++;
                        } else {
                            local_current_day_int++;
                            local_current_day_string_to_hash_map++;
                        }
                    }

                    if (local_current_day_string_to_hash_map == 8) {
                        local_current_day_string_to_hash_map = 1;
                    }
                }

                break;
        }
        /*for (String appointment_date : appointment_dates) {
            Log.d("DATESSSS", appointment_date);
        }*/
        return appointment_dates;
    }
}

