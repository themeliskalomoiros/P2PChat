package gr.kalymnos.skemelio.p2pchat.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Message implements Parcelable, Serializable {

    private String message, sender;

    public Message(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    protected Message(Parcel in) {
        message = in.readString();
        sender = in.readString();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
        parcel.writeString(sender);
    }
}
