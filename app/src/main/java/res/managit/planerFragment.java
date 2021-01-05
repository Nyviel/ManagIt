package res.managit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

import res.managit.add.event.adapter.EventAdapter;
import res.managit.service.EventRetriever;
import res.managit.dbo.PublicDatabaseAcces;
import res.managit.dbo.WarehouseDb;
import res.managit.dbo.entity.Event;
import res.managit.materials.DrawableUtils;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link planerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class planerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //added calendar variable
    private CalendarView calendarView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ListView listEvents;
    private EventAdapter adapterToEventsList;

    public planerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment planerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static planerFragment newInstance(String param1, String param2) {
        planerFragment fragment = new planerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_planer, container, false);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);

        //list with calendar events not databases
        List<EventDay> events = new ArrayList<>();
        WarehouseDb db = PublicDatabaseAcces.currentDatabase;

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Event> eventList = db.eventDao().getAll();
            for (Event event : eventList) {
                LocalDateTime date = event.getDate();
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(date.getYear(),
                        date.getMonthValue() - 1,
                        date.getDayOfMonth(),
                        date.getHour(),
                        date.getMinute());
                events.add(new EventDay(calendar1, DrawableUtils.getCircleDrawableWithText(requireActivity()
                                .getApplicationContext(),
                        event.getAction().equals("loading") ? "L" : "UL")));

            }
        });

        //set how many months we can see. In that case 1 previous and 5 next
        Calendar min = Calendar.getInstance();
        min.add(Calendar.MONTH, -1);
        Calendar max = Calendar.getInstance();
        max.add(Calendar.MONTH, 5);
        calendarView.setMinimumDate(min);
        calendarView.setMaximumDate(max);

        //add icons/events to dates
        calendarView.setEvents(events);

        //initialize variable to events lists
        listEvents = (ListView) view.findViewById(R.id.eventsList);
        ArrayList<Event> eventsListInCurrentDate = new ArrayList<>();
        adapterToEventsList = new EventAdapter(this.getContext(), eventsListInCurrentDate);
        listEvents.setAdapter(adapterToEventsList);

        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {

                View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.event_popup, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                popupWindow.showAsDropDown(popupView,0,0);

                Button close = popupView.findViewById(R.id.close);
                close.setOnClickListener((event) -> {
                    popupWindow.dismiss();
                });

                Event value = (Event) adapter.getItemAtPosition(position);

                new EventRetriever(popupView, PublicDatabaseAcces.currentDatabase, value).execute();

            }
        });


        calendarView.setOnDayClickListener(eventDay ->
                Executors.newSingleThreadExecutor().execute(() -> {
                    List<Event> eventList = db.eventDao().getAll();
                    List<Event> chosenEvents = new ArrayList<>();
                    LocalDateTime dateTime = LocalDateTime.of(eventDay.getCalendar()
                                    .get(Calendar.YEAR), eventDay
                                    .getCalendar()
                                    .get(Calendar.MONTH) + 1,
                            eventDay.getCalendar().get(Calendar.DATE),
                            eventDay.getCalendar().get(Calendar.HOUR),
                            eventDay.getCalendar().get(Calendar.MINUTE));

                    for (Event event : eventList) {
                        if (event.getDate().getDayOfYear() == dateTime.getDayOfYear() && event.getDate().getYear() == dateTime.getYear()) {
                            //trzeba ustawiac text poza tym Executors bo inaczej wywala error z tym ze tylko głowny watek moze miec dostep do view
                            chosenEvents.add(event);
                        }
                    }
                    setEventList(chosenEvents);
                })
        );

        //set button to add event
        FloatingActionButton fab = view.findViewById(R.id.add_event_button_start_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddEventFirstStepActivity();
            }
        });
        return view;
    }

    //function to set text in main thread
    private void setEventList(List<Event> events) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapterToEventsList.clear();
                adapterToEventsList.addAll(events);
            }
        });
    }

    private void openAddEventFirstStepActivity(){
        Intent intent = new Intent(getActivity(), AddEventFirstStepActivity.class);
        startActivity(intent);
    }

}