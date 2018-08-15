package sg.edu.rp.webservices.dmsdchatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MessageAdapter extends ArrayAdapter<ChatMessage> {

    private static class ViewHolder {
        TextView tvName, tvDate, tvMessage;

    }

    public MessageAdapter(Context context, ArrayList<ChatMessage> messages) {
        super(context, R.layout.activity_msg, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ChatMessage user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.activity_msg, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.message_user);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.message_time);
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.message_text);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.tvName.setText(user.getMessageUser());
        viewHolder.tvDate.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", user.getMessageTime()));
        viewHolder.tvMessage.setText(user.getMessageText());

        // Return the completed view to render on screen
        return convertView;
    }
}