package jin.hlschedule.Class;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;


public class SubjectClass implements Parcelable {
    public String num;
    public String major;
    public String name;
    public String ban;
    public int hak;
    public String isu;
    public String professor;
    public String time;
    public String classroom;
    public String onoff;
    public  String[] timetable = new String[10];
    public boolean bool = false;

    public SubjectClass(String name) {
        this.name = name;
    }

    public SubjectClass(Parcel in) {
        readFromParcel(in);
    }

    public SubjectClass(String num, String major, String name, String ban, int hak, String isu, String professor, String time, String classroom, String onoff) {
        this.num = num;
        this.major = major;
        this.name = name;
        this.ban = ban;
        this.hak = hak;
        this.isu = isu;
        this.professor = professor;
        this.time = time;
        this.classroom = classroom;
        this.onoff = onoff;
        Arrays.fill(timetable, "");
        String[] str;
        String strTmp = "";

        str = this.time.split(" ");

        for (int i = 0; i < str.length; i++) {
            if (str[i].contains(","))
                str[i] = str[i].replace(",", "," + str[i].charAt(0));

        }
        for (int i = 1; i < str.length; i++) {
           str[i] =  ',' + str[i];
        }

        for (int i = 0; i < (str.length); i++) {
            strTmp += str[i];
        }


        this.timetable = strTmp.split(",");

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(num);
        dest.writeString(major);
        dest.writeString(name);
        dest.writeString(ban);
        dest.writeInt(hak);
        dest.writeString(isu);
        dest.writeString(professor);
        dest.writeString(time);
        dest.writeString(classroom);
    }

    private void readFromParcel(Parcel in) {
        num = in.readString();
        major = in.readString();
        name = in.readString();
        ban = in.readString();
        hak = in.readInt();
        isu = in.readString();
        professor = in.readString();
        time = in.readString();
        classroom = in.readString();
    }

    public static final Parcelable.Creator<SubjectClass> CREATOR = new Parcelable.Creator<SubjectClass>() {
        @Override
        public SubjectClass createFromParcel(Parcel source) {

            return new SubjectClass(source);
        }

        @Override
        public SubjectClass[] newArray(int size) {
            return new SubjectClass[size];
        }
    };
}