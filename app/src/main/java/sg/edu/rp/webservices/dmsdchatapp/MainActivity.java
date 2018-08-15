package sg.edu.rp.webservices.dmsdchatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fbAuth;
    FirebaseUser fbUser;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference messageListRef, nameRef;

    EditText etMessage;
    Button btnSend;
    String msguser;
    Long time;
    ArrayList<ChatMessage> alMessage = new ArrayList<ChatMessage>();
    private ChatMessage message;
    ArrayAdapter aaMessage;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fbAuth = FirebaseAuth.getInstance();

        fbUser = fbAuth.getCurrentUser();
        String uid = fbUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        etMessage = (EditText) findViewById(R.id.etName);
        btnSend = (Button) findViewById(R.id.btnSend);
        lv = (ListView) findViewById(R.id.listView);

        alMessage = new ArrayList<ChatMessage>();
        aaMessage = new MessageAdapter(getBaseContext(), alMessage);
        lv.setAdapter(aaMessage);
        nameRef = firebaseDatabase.getReference("profiles/" + uid);
        messageListRef = firebaseDatabase.getReference("messages/");

        nameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msguser = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = etMessage.getText().toString();

                time = new Date().getTime();

                ChatMessage messages = new ChatMessage(msg, time, msguser);
                messageListRef.push().setValue(messages);
                etMessage.setText("");


            }
        });


        messageListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("CommunicateFragment", "onChildAdded");
                ChatMessage msg = dataSnapshot.getValue(ChatMessage.class);
                if (msg != null) {
                    msg.setId(dataSnapshot.getKey());
                    alMessage.add(msg);
                    aaMessage.notifyDataSetChanged();
                    scrollMyListViewToBottom();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String selectedId = dataSnapshot.getKey();
                ChatMessage msg = dataSnapshot.getValue(ChatMessage.class);

                if (msg != null) {
                    for (int i = 0; i < alMessage.size(); i++) {
                        if (alMessage.get(i).getId().equals(selectedId)) {
                            msg.setId(selectedId);
                            alMessage.set(i, msg);

                        }
                    }

                    aaMessage.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.i("MainActivity", "onChildRemoved()");

                String selectedId = dataSnapshot.getKey();
                ChatMessage msg = dataSnapshot.getValue(ChatMessage.class);
                if (msg != null) {
                    for (int i = 0; i < alMessage.size(); i++) {
                        if (alMessage.get(i).getId().equals(selectedId)) {
                            msg.setId(selectedId);
                            alMessage.remove(i);
                        }
                    }
                    aaMessage.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_logout) {

            logout();

            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(i);
        } else {


            Intent i = new Intent(getBaseContext(), MainActivity.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        fbAuth.signOut();
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Are you sure you want to delete?");
        menu.add(0, v.getId(), 0, "Delete");
    }

    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        String msguser1 = alMessage.get(index).getMessageUser();
        Toast.makeText(MainActivity.this, "msg sender: " + msguser + "/ncurrent user: " + msguser1, Toast.LENGTH_LONG).show();
        int id = item.getItemId();
        if (msguser1.equals(msguser)) {
            if (id == 0) {


                messageListRef.child(alMessage.get(index).getId()).removeValue();
            }
            return true;


        } else if (!msguser1.equals(msguser)) {
            Toast.makeText(MainActivity.this, "You cannot delete other user's msg!", Toast.LENGTH_LONG).show();
        }


        return super.onContextItemSelected(item);
    }
    private void scrollMyListViewToBottom() {
        lv.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lv.setSelection(aaMessage.getCount() - 1);
            }
        });
    }
}


