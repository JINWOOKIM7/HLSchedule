package jin.hlschedule.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper {
    private static final String DATABASE_NAME = "list.sqlite";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    public DB mDBHelper;
    private Context mCtx;

    private class DB extends SQLiteOpenHelper {

        public DB(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }
    }

    public DBHelper(Context context) {
        this.mCtx = context;
    }

    public DBHelper open() throws SQLException {
        mDBHelper = new DB(mCtx);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDB.close();
    }

    public Cursor getAllColumns() {
        Cursor c = mDB.rawQuery("select * from addlist", null);

        return c;
    }

    public Cursor deleteColumns() {
        //delete from list where 대상학과 = '교양기초교육대학(---)' AND 교과목명 = '대학영어2' AND 분반 = '01';
        Cursor c = mDB.rawQuery("delete from addlist;", null);
        return c;
    }

    public Cursor deleteColumns(String major, String name, String ban) {
        //delete from list where 대상학과 = '교양기초교육대학(---)' AND 교과목명 = '대학영어2' AND 분반 = '01';
        Cursor c = mDB.rawQuery("delete from addlist where 대상학과 = '" + major + "' AND 교과목명 = '" + name + "'  AND 분반 = '" + ban + "';", null);
        Log.v("database", "delete from addlist where 대상학과 = '" + major + "' AND 교과목명 = '" + name + "'  AND 분반 = '" + ban + "';");
        return c;
    }

    public Cursor getMajorColumns(String s2) {
        Cursor c = mDB.rawQuery("select * from list where (이수구분 = '공통전필' or 이수구분 = '전선'  or 이수구분 = '전필' ) AND 대상학과 like \"%" + s2 + "%\"", null);
        return c;
    }

    public Cursor getEssenColumns(String s2) {
        Cursor c = mDB.rawQuery("select * from list where (이수구분 = '필수기초') AND 교과목명 like \"%" + s2 + "%\"", null);
        return c;
    }

    public Cursor getSelectColumns(String s2) {
        Cursor c = null;
        if (s2.equals("어문")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '선택기초') AND (" +
                    "교과목명 = '영어읽기연습' or " +
                    "교과목명 = '실용영어회화' or " +
                    "교과목명 = '영어발표와토론' or " +
                    "교과목명 = '일어1' or " +
                    "교과목명 = '일어2' or " +
                    "교과목명 = '중국어2' or " +
                    "교과목명 = '중국어1' or " +
                    "교과목명 = '취업영어1' or " +
                    "교과목명 = '스크린영어' or " +
                    "교과목명 = '영작문1' or " +
                    "교과목명 = '기초영문법' or " +
                    "교과목명 = '영작문2' or " +
                    "교과목명 = '생활한문' or " +
                    "교과목명 = '러시아어'" +
                    ")", null);
        } else if (s2.equals("정보소통")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '선택기초') AND (" +
                    "교과목명 = '컴퓨팅사고와문제해결' or " +
                    "교과목명 = '창의코딩파이썬' or " +
                    "교과목명 = '창의코딩스크래치/앱' or " +
                    "교과목명 = '디지털스마트워크' or " +
                    "교과목명 = '데이터분석과활용' or " +
                    "교과목명 = '정보소통의이해' or " +
                    "교과목명 = '오피스마스터1' or " +
                    "교과목명 = '오피스마스터2' or " +
                    "교과목명 = '프리젠테이션기초' or " +
                    "교과목명 = '스프레드시트기초' or " +
                    "교과목명 = '디지털문서작성' or " +
                    "교과목명 = '디지털그래픽활용'" +
                    ")", null);
        } else if (s2.equals("기초과학")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '선택기초') AND (" +
                    "교과목명 = '통계학입문' or " +
                    "교과목명 = '일반화학및실험2' or " +
                    "교과목명 = '일반생물학및실험2' or " +
                    "교과목명 = '일반통계학및실습1' or " +
                    "교과목명 = '일반통계학및실습2' or " +
                    "교과목명 = '물리학및실험2' or " +
                    "교과목명 = '일반수학및실습2' or " +
                    "교과목명 = '기초공학수학1' or " +
                    "교과목명 = '일반물리학2' or " +
                    "교과목명 = '기초미생물학' or " +
                    "교과목명 = '기초수학'" +
                    ")", null);
        }
        return c;
    }

    public Cursor getCoreColumns(String s2) {
        Cursor c = null;
        if (s2.equals("(핵심)융복합")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '핵심교양') AND (" +
                    "교과목명 = '서양소설속의사랑과결혼' or " +
                    "교과목명 = '고전속인물탐구' or " +
                    "교과목명 = '사랑학개론' or " +
                    "교과목명 = '아시아공동체론' or " +
                    "교과목명 = '여행과문명' or " +
                    "교과목명 = '소셜미디어와테크놀로지' or " +
                    "교과목명 = '인생의사계절' or " +
                    "교과목명 = '융복합적문제해결의프랙시스' or " +
                    "교과목명 = '생활속의통계학' or " +
                    "교과목명 = '테마로보는한국인물사' or " +
                    "교과목명 = '한국사회와21세기' or " +
                    "교과목명 = '환경과문학' or " +
                    "교과목명 = '세계시민의식과글로벌교육'" +
                    ")", null);
        } else if (s2.equals("(핵심)인문학")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '핵심교양') AND (" +
                    "교과목명 = '문학의이해' or " +
                    "교과목명 = '논리와비판적사고' or " +
                    "교과목명 = '철학의이해' or " +
                    "교과목명 = '인터넷시대의호머읽기' or " +
                    "교과목명 = '세계화와언어' or " +
                    "교과목명 = '명저읽기세미나' or " +
                    "교과목명 = '전통문화와한문학' or " +
                    "교과목명 = '유학과동아시아' or " +
                    "교과목명 = '한국사의쟁점' or " +
                    "교과목명 = '집중한문' or " +
                    "교과목명 = '현대사회와윤리' or " +
                    "교과목명 = '그리스로마신화' or " +
                    "교과목명 = '동북아의영토분쟁' or " +
                    "교과목명 = '중국의문화와예술'" +
                    ")", null);
        } else if (s2.equals("(핵심)사회과학")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '핵심교양') AND (" +
                    "교과목명 = '현대사회의이해' or " +
                    "교과목명 = '현대사회와사회복지' or " +
                    "교과목명 = '뉴미디어와미래사회' or " +
                    "교과목명 = '인권과법' or " +
                    "교과목명 = '외교와협상' or " +
                    "교과목명 = '한국경제의현재와미래'" +
                    ")", null);
        } else if (s2.equals("(핵심)자연과학과공학")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '핵심교양') AND (" +
                    "교과목명 = '융복합트랜드' or " +
                    "교과목명 = '식생활과문화' or " +
                    "교과목명 = '생활속의화학' or " +
                    "교과목명 = '과학적성지식' or " +
                    "교과목명 = '즐거운수학적사고' or " +
                    "교과목명 = '컴퓨터시스템의개념적인이해' or " +
                    "교과목명 = '인체생로병사' or " +
                    "교과목명 = '수학과사회' or " +
                    "교과목명 = '생명의본질' or " +
                    "교과목명 = '생활속의컴퓨터' or " +
                    "교과목명 = '생활속의IT디자인'" +
                    ")", null);
        }
        return c;
    }

    public Cursor getGeneralColumns(String s2) {
        Cursor c = null;
        if (s2.equals("인문학")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '일반교양') AND (" +
                    "교과목명 = '현대일본의사회와문화' or " +
                    "교과목명 = '영상문학' or " +
                    "교과목명 = '서양지성사' or " +
                    "교과목명 = '죽음의철학적접근' or " +
                    "교과목명 = '중국문학산책' or " +
                    "교과목명 = '현대중국사회의이해' or " +
                    "교과목명 = '러시아의이해' or " +
                    "교과목명 = '신뢰와번영' or " +
                    "교과목명 = '러시아와동북아관계의이해' or " +
                    "교과목명 = '노장사상의이해' or " +
                    "교과목명 = '미학의이해' or " +
                    "교과목명 = '한국어의문화적이해' or " +
                    "교과목명 = '교양고고학' or " +
                    "교과목명 = '일본과동아시아' or " +
                    "교과목명 = '한시의이해와감상' or " +
                    "교과목명 = '동양문화사' or " +
                    "교과목명 = '서양문화사' or " +
                    "교과목명 = '한국사의이해' or " +
                    "교과목명 = '일본문화사' or " +
                    "교과목명 = '고급작문' or " +
                    "교과목명 = '영문대중소설' or " +
                    "교과목명 = '논술의기초'" +
                    ")", null);
        }
        else if (s2.equals("사회과학")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '일반교양') AND (" +
                    "교과목명 = '대학생을위한실용금융' or " +
                    "교과목명 = '사랑과법' or " +
                    "교과목명 = '경제학개론' or " +
                    "교과목명 = '사회학개론' or " +
                    "교과목명 = '사회속의법의이해' or " +
                    "교과목명 = '현대사회와여성' or " +
                    "교과목명 = '행정학개론' or " +
                    "교과목명 = '교육학의이해' or " +
                    "교과목명 = '직업과사회' or " +
                    "교과목명 = '청년의심리' or " +
                    "교과목명 = '현대사회의정치' or " +
                    "교과목명 = '정책학개론' or " +
                    "교과목명 = '북한개론' or " +
                    "교과목명 = '심리학의이해' or " +
                    "교과목명 = '생활과경제' or " +
                    "교과목명 = '지역사회와노인복지' or " +
                    "교과목명 = '외국문화와국제매너' or " +
                    "교과목명 = '현대방송의이해' or " +
                    "교과목명 = '독일분단통일현장과한반도' or " +
                    "교과목명 = '법과생활' or " +
                    "교과목명 = '국제사회와법' or " +
                    "교과목명 = '국제개발협력의이해' or " +
                    "교과목명 = '디지털게임과커뮤니케이션' or " +
                    "교과목명 = '경영학원론' or " +
                    "교과목명 = '관광학개론'" +
                    ")", null);
        }
        else if (s2.equals("자연과학")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '일반교양') AND (" +
                    "교과목명 = '인공지능' or " +
                    "교과목명 = '과학기술과사회' or " +
                    "교과목명 = '생물학의이해' or " +
                    "교과목명 = '지구촌환경재난' or " +
                    "교과목명 = '환경과에너지' or " +
                    "교과목명 = '현대생활과운동' or " +
                    "교과목명 = '생활속의바이오테크' or " +
                    "교과목명 = '스포츠와인성함양' or " +
                    "교과목명 = '웰빙스포츠론' or " +
                    "교과목명 = '원자에서우주까지' or " +
                    "교과목명 = '스포츠속의과학지식' or " +
                    "교과목명 = '인터넷의이해'" +
                    ")", null);
        }
        else if (s2.equals("문화및예술")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '일반교양') AND (" +
                    "교과목명 = '몽골문화의이해' or " +
                    "교과목명 = '음악의이해' or " +
                    "교과목명 = '연극의이해' or " +
                    "교과목명 = '영화의이해' or " +
                    "교과목명 = '서양미술의이해' or " +
                    "교과목명 = '한국문화의형성과 발전' or " +
                    "교과목명 = '대중문화론' or " +
                    "교과목명 = '사진예술' or " +
                    "교과목명 = '매스미디어와현대사회' or " +
                    "교과목명 = '러시아문화와예술'" +
                    ")", null);
        }
        else if (s2.equals("국제화")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '일반교양') AND (" +
                    "교과목명 = 'Intermediate Academic Research' or " +
                    "교과목명 = 'Natural Science' or " +
                    "교과목명 = 'Basic Spanish 2' or " +
                    "교과목명 = 'International Affairs and Global Perspectives' or " +
                    "교과목명 = 'Issues In World History' or " +
                    "교과목명 = 'Understanding Basic Mathematics & Statistics' or " +
                    "교과목명 = 'Understanding Information Society' or " +
                    "교과목명 = 'Introduction to Computing & Programming'" +
                    ")", null);
        }
        else if (s2.equals("한국어및한국문화")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '일반교양') AND (" +
                    "교과목명 = '한국문학의이해' or " +
                    "교과목명 = '매체를통한한국사회의이해' or " +
                    "교과목명 = '집중초급한국어1' or " +
                    "교과목명 = '집중초급한국어2' or " +
                    "교과목명 = '(외국인을위한)한국대학생활의이해' or " +
                    "교과목명 = '외국인을위한논리적표현의이해' or " +
                    "교과목명 = '기초한국어' or " +
                    "교과목명 = '한국의문화유산' or " +
                    "교과목명 = '한국지역학입문' or " +
                    "교과목명 = '한국의민속'" +
                    ")", null);
        }
        else if (s2.equals("진로탐색")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '일반교양') AND (" +
                    "교과목명 = '기업가정신과창업' or " +
                    "교과목명 = '취업과진로1' or " +
                    "교과목명 = '취업과진로2' or " +
                    "교과목명 = '성공취업전략1' or " +
                    "교과목명 = '성공취업전략2' or " +
                    "교과목명 = '성공취업전략3' or " +
                    "교과목명 = '취업실무의이해1' or " +
                    "교과목명 = '취업실무의이해2' or " +
                    "교과목명 = '사회적경제와협동조합' or " +
                    "교과목명 = '행복한직업인이되는길' or " +
                    "교과목명 = '바이오산업과창업' or " +
                    "교과목명 = '콘텐츠의이해와창업' or " +
                    "교과목명 = '내인생과기업가정신'" +
                    ")", null);
        }
        else if (s2.equals("특별프로그램")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '일반교양') AND (" +
                    "교과목명 = '생활영어4' or " +
                    "교과목명 = '생활중국어4' or " +
                    "교과목명 = '생활일본어 4' or " +
                    "교과목명 = '생활러시아어 4'" +
                    ")", null);
        }
        return c;
    }

    public Cursor getSpecialColumns(String s2) {
        Cursor c = null;
        if (s2.equals("영상문예창작")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '복수전공1') AND (" +
                    "교과목명 = '디지털스토리텔링창작연습' or " +
                    "교과목명 = '방송영상대본작법Ⅱ'" +
                    ")", null);
        }
        else if (s2.equals("청소년학")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '복수전공1') AND (" +
                    "교과목명 = '청소년지도현장실습'" +
                    ")", null);
        }
        else if (s2.equals("한중비지니스")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '복수전공1') AND (" +
                    "교과목명 = '국제통상론' or " +
                    "교과목명 = '무역결제론' or " +
                    "교과목명 = '한중비지니스특강1'" +
                    ")", null);
        }
        else if (s2.equals("디지털게임")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '복수전공1') AND (" +
                    "교과목명 = '게임기획과운영' or " +
                    "교과목명 = '게임분석과평가'" +
                    ")", null);
        }
        else if (s2.equals("디지털북")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '복수전공1') AND (" +
                    "교과목명 = '디지털북디자인 II' or " +
                    "교과목명 = '디지털북프로젝트'" +
                    ")", null);
        }
        else if (s2.equals("문화콘텐츠교육사")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '복수전공1') AND (" +
                    "교과목명 = '박물관교육콘텐츠개발론' or " +
                    "교과목명 = '문화예술콘텐츠론' or " +
                    "교과목명 = '한국미술사교육론'" +
                    ")", null);
        }
        else if (s2.equals("LOHAS서비스전문가")) {
            c = mDB.rawQuery("select * from list where (" +
                    "교과목명 = '헬스케어서비스산업론' or " +
                    "교과목명 = '웰니스창업' or " +
                    "교과목명 = '헬스케어취업' or " +
                    "교과목명 = '헬스케어캡스톤디자인' or " +
                    "교과목명 = '기업의사회적혁신마케팅론' or " +
                    "교과목명 = '헬스케어서비스산업마케팅' or " +
                    "교과목명 = 'LOHAS서비스개발론' or " +
                    "교과목명 = 'LOHAS광고기획론'" +
                    ")", null);
        }
        else if (s2.equals("헬스케어바이오제품전문가")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '복수전공1') AND (" +
                    "교과목명 = '기초바이오헬스케어' or " +
                    "교과목명 = '바이오헬스케어제품마케팅론'" +
                    ")", null);
        }
        else if (s2.equals("유헬스ICT서비스전문가")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '복수전공1') AND (" +
                    "교과목명 = '유헬스ICT기초' or " +
                    "교과목명 = '유헬스센싱시스템' or " +
                    "교과목명 = '유헬스착용형장치개론'" +
                    ")", null);
        }
        else if (s2.equals("르네상스인문학")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '복수전공1') AND (" +
                    "교과목명 = '인문고전과전인적인간' or " +
                    "교과목명 = '문학적비평과철학적사유' or " +
                    "교과목명 = '고전강독1' or " +
                    "교과목명 = '문화콘텐츠현장실습2' or " +
                    "교과목명 = '졸업콘텐츠발표지도'" +
                    ")", null);
        }
        else if (s2.equals("고령화친화서비스")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '복수전공1') AND (" +
                    "교과목명 = '고령친화서비스의이해' or " +
                    "교과목명 = '노년의이해' or " +
                    "교과목명 = 'SMART취·창업' or " +
                    "교과목명 = '고령친화지역산업의이해'" +
                    ")", null);
        }
        else if (s2.equals("동북아지역")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '부전공') AND (" +
                    "교과목명 = '동북아역사와문화' or " +
                    "교과목명 = '동북아문화융합세미나' or " +
                    "교과목명 = '동북아국제관계'" +
                    ")", null);
        }
        else if (s2.equals("광고홍보학과")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '전선'  or 이수구분 = '전필' )  AND ((대상학과 = '광고홍보학과(*-*)')  OR (대상학과 = '광고홍보학과(---)')) AND (" +
                    "교과목명 = '오디세이세미나3' or " +
                    "교과목명 = '졸업논문및현장실습' or " +
                    "교과목명 = 'IAA International Communication' or " +
                    "교과목명 = '광고개론' or " +
                    "교과목명 = '홍보개론' or " +
                    "교과목명 = '매체계획론' or " +
                    "교과목명 = '광고촉진론' or " +
                    "교과목명 = '광고카피작성II' or " +
                    "교과목명 = '홍보특강' or " +
                    "교과목명 = '직접반응마케팅' or " +
                    "교과목명 = '국제마케팅커뮤니케이션특강' or " +
                    "교과목명 = '디지털소비자분석(종합설계)' or " +
                    "교과목명 = '조직커뮤니케이션' or " +
                    "교과목명 = '캡스톤디자인-TV CF제작' or " +
                    "교과목명 = '크리에이티브원론' or " +
                    "교과목명 = 'PR콘텐츠작성실습' or " +
                    "교과목명 = '광고홍보영상기초' or " +
                    "교과목명 = '광고홍보그래픽커뮤니케이션' or " +
                    "교과목명 = '아이디어발상법' or " +
                    "교과목명 = 'Consumer Behavior'" +
                    ")", null);
        }
        return c;
    }

    public Cursor getHallymColumns(String s2) {
        Cursor c = null;
        if (s2.equals("(시민)독서")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '한림소양') AND (" +
                    "교과목명 = '한림북클럽1' or " +
                    "교과목명 = '한림북클럽2' or " +
                    "교과목명 = '한림북클럽3'" +
                    ")", null);
        }
        else if (s2.equals("(시민)글로벌")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '한림소양') AND (" +
                    "교과목명 = '글로벌문화의이해2'" +
                    ")", null);
        }
        else if (s2.equals("(시민)봉사")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '한림소양') AND (" +
                    "교과목명 = '사회봉사활동Ⅰ' or " +
                    "교과목명 = '사회봉사활동Ⅱ' or " +
                    "교과목명 = '자율형봉사인증'" +
                    ")", null);
        }
        else if (s2.equals("(시민)체육")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '한림소양') AND (" +
                    "교과번호 = '006173' or " +
                    "교과목명 = '스포츠-축구' or " +
                    "교과목명 = '스포츠-농구' or " +
                    "교과목명 = '스포츠-스키캠프' or " +
                    "교과목명 = '스포츠-수영' or " +
                    "교과목명 = '스포츠-테니스' or " +
                    "교과목명 = '스포츠-배드민턴' or " +
                    "교과목명 = '스포츠-골프' or " +
                    "교과목명 = '스포츠-스쿼시' or " +
                    "교과목명 = '스포츠-등반및MTB' or " +
                    "교과목명 = 'Team Sports II' or " +
                    "교과목명 = 'Team Sports IV' or " +
                    "교과목명 = '스포츠-댄스스포츠' or " +
                    "교과목명 = '다이어트휘트니스'" +
                    ")", null);
        }
        else if (s2.equals("생애설계")) {
            c = mDB.rawQuery("select * from list where (이수구분 = '한림소양') AND (" +
                    "교과목명 = '오디세이세미나2(리더십과 기업가정신)'" +
                    ")", null);
        }
        return c;
    }
    public Cursor getEtcColumns(String s2) {
        Cursor c = null;
        if(s2.equals("사이버")){
            c = mDB.rawQuery("select * from list where 온오프 = '온라인' or 온오프 = '온오프겸용'", null);
        }
        else{
            c = mDB.rawQuery("select * from list where 대상학과 like \"%" + s2 + "%\"", null);
        }
        return c;
    }
    public Cursor getSearchColumns(String s2) {
        Cursor c = mDB.rawQuery("select * from list where 담당교수 =  '"+ s2 +"' or 교과목명 like \"%" + s2 + "%\"", null);
        return c;
    }
    public Cursor getMaxCase() {
        Cursor c = mDB.rawQuery("select 수 from savelist", null);
        return c;
    }
    public Cursor getCase() {
        Cursor c = mDB.rawQuery("select * from savelist", null);
        return c;
    }
    public Cursor getCase(int cases) {
        Cursor c = mDB.rawQuery("select * from savelist where 수 = " + cases, null);
        return c;
    }
    public Cursor deleteSave() {
        //delete from list where 대상학과 = '교양기초교육대학(---)' AND 교과목명 = '대학영어2' AND 분반 = '01';
        Cursor c = mDB.rawQuery("delete from savelist;", null);
        return c;
    }
}
