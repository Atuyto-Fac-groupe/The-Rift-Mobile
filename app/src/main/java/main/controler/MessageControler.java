package main.controler;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import com.google.gson.Gson;
import main.App;
import main.model.Message;
import main.view.FragmentMessage;
import main.view.MainActivity;

public class MessageControler implements View.OnTouchListener {

    private EditText editText;
    private static Gson gson;
    public MessageControler(EditText editText, FragmentMessage fragmentMessage) {
        this.editText = editText;
        gson = new Gson();

        this.updateMessageRead();

    }

    private void updateMessageRead(){
        for (Message message : App.player.getMessages()){
            message.setSee(true);
        }
        MainActivity.getInstance().setBadgeMessageOnRead();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width())) {

                senMessage();
                Log.d("idMessage", "message en cours ...");

                return true;
            }
        }
        return false;
    }

    private void senMessage() {
        String message = editText.getText().toString();
        if (!message.isEmpty()) {
            Message m = new Message(message, "2", "1");
            m.setSee(true);
            String json = gson.toJson(m);
            App.socketManager.sendMessage(json);
            editText.setText("");
        }
    }

}
