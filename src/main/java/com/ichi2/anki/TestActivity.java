package com.ichi2.anki;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ichi2.async.DeckTask;
import com.ichi2.libanki.*;
import com.ichi2.libanki.Collection;
import com.ichi2.themes.Themes;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;

import java.util.*;

/**
 * Created by gengke on 14-3-19.
 */
public class TestActivity extends AnkiActivity {
    private TextView mTextBarRed;
    private TextView mTextBarBlack;
    private TextView mTextBarBlue;

    private GridView listView;
    private ImageAdapter mAdapter = new ImageAdapter();
    private Sched mSched;
    private String mBaseUrl;
    private List<String> imageUrls = new ArrayList<String>();
    private Collection mCol;

    public static final int EASE_FAILED = 1;
    public static final int EASE_HARD = 2;
    public static final int EASE_MID = 3;
    public static final int EASE_EASY = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Themes.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        // The hardware buttons should control the music volume while reviewing.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mTextBarRed = (TextView) findViewById(R.id.red_number);
        mTextBarBlack = (TextView) findViewById(R.id.black_number);
        mTextBarBlue = (TextView) findViewById(R.id.blue_number);



        listView = (GridView) findViewById(R.id.gridview);
        ((GridView) listView).setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mCurrentURL.equals(imageUrls.get(position))){
                    Toast.makeText(parent.getContext(), "bingo", Toast.LENGTH_SHORT).show();
                    DeckTask.launchDeckTask(DeckTask.TASK_TYPE_ANSWER_CARD, mRenderCardHandler, new DeckTask.TaskData(mSched,
                            mCurrentCard, EASE_EASY));
                }
            }
        });

        mCol = AnkiDroidApp.getCol();
        if (mCol == null) {
            return;
        } else {
            mSched = mCol.getSched();

            mBaseUrl = Utils.getBaseUrl(mCol.getMedia().getDir());
        }
        DeckTask.launchDeckTask(DeckTask.TASK_TYPE_ANSWER_CARD, mRenderCardHandler, new DeckTask.TaskData(mSched,
                null, 0));
    }

    public class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            View view = convertView;
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.item_grid_image, parent, false);
                holder = new ViewHolder();
                assert view != null;
                holder.imageView = (ImageView) view.findViewById(R.id.image);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            ImageLoader.getInstance().displayImage(imageUrls.get(position), holder.imageView);

            return view;
        }

        class ViewHolder {
            ImageView imageView;
        }
    }

    private Card mCurrentCard;

    private String mCurrentURL;
    private DeckTask.TaskListener mRenderCardHandler = new DeckTask.TaskListener() {

        @Override
        public void onPreExecute() {

        }

        @Override
        public void onPostExecute(DeckTask.TaskData result) {
            DeckTask.launchDeckTask(DeckTask.TASK_TYPE_SEARCH_CARDS, mGetCardsHandler, new DeckTask.TaskData(
                    new Object[] { mCol, new HashMap<String, String>(), "", "" }));
        }

        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {
            updateForNewCard();

            imageUrls.clear();

            mCurrentCard = values[0].getCard();

            Sound.resetSounds();
            Sound.parseSounds(mBaseUrl, mCurrentCard.getAnswer(true), false, 0);
            Sound.playSounds(0);

            mCurrentURL = mBaseUrl + Uri.encode(mCurrentCard.getQuestion(true).split("'")[1]);
            imageUrls.add(mCurrentURL);
        }
    };

    private DeckTask.TaskListener mGetCardsHandler = new DeckTask.TaskListener() {
        @Override
        public void onPreExecute() {

        }

        @Override
        public void onPostExecute(DeckTask.TaskData result) {

        }

        @Override
        public void onProgressUpdate(DeckTask.TaskData... values) {

            ArrayList<HashMap<String, String>> mCards = new ArrayList<HashMap<String, String>>();
            ArrayList<Integer> nums = new ArrayList<Integer>();
            Random rd = new Random();
            do {
                Integer num = rd.nextInt(values[0].getCards().size());
                if (!nums.contains(num)) nums.add(num);
            } while (nums.size() < 10);

            for(Integer num : nums) {
                mCards.add(values[0].getCards().get(num));
            }

            for(HashMap<String, String> map : mCards) {
                String imgURL = mBaseUrl + Uri.encode(map.get("sfld").replaceAll(" ", ""));
                if (!imageUrls.contains(imgURL))    imageUrls.add(imgURL);
            }

            Collections.shuffle(imageUrls);

            mAdapter.notifyDataSetChanged();
        }
    };

    private void updateForNewCard() {
        updateScreenCounts();

        //updateStatisticBars();

    }

    private void updateScreenCounts() {
        if (mCurrentCard == null) {
            return;
        }

        try {
            String[] title = mSched.getCol().getDecks().get(mCurrentCard.getDid()).getString("name").split("::");
            //AnkiDroidApp.getCompat().setTitle(this, title[title.length - 1], mInvertedColors);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        int[] counts = mSched.counts(mCurrentCard);

        int eta = mSched.eta(counts, false);
        //AnkiDroidApp.getCompat().setSubtitle(this, getResources().getQuantityString(R.plurals.reviewer_window_title, eta, eta), mInvertedColors);

        SpannableString newCount = new SpannableString(String.valueOf(counts[0]));
        SpannableString lrnCount = new SpannableString(String.valueOf(counts[1]));
        SpannableString revCount = new SpannableString(String.valueOf(counts[2]));
//        if (mPrefHideDueCount) {
//            revCount = new SpannableString("???");
//        }

        switch (mCurrentCard.getQueue()) {
            case Card.TYPE_NEW:
                newCount.setSpan(new UnderlineSpan(), 0, newCount.length(), 0);
                break;
            case Card.TYPE_LRN:
                lrnCount.setSpan(new UnderlineSpan(), 0, lrnCount.length(), 0);
                break;
            case Card.TYPE_REV:
                revCount.setSpan(new UnderlineSpan(), 0, revCount.length(), 0);
                break;
        }

        mTextBarRed.setText(newCount);
        mTextBarBlack.setText(lrnCount);
        mTextBarBlue.setText(revCount);
    }

//    private void updateStatisticBars() {
//        if (mStatisticBarsMax == 0) {
//            View view = findViewById(R.id.progress_bars_back1);
//            mStatisticBarsMax = view.getWidth();
//            mStatisticBarsHeight = view.getHeight();
//        }
//        float[] progress = mSched.progressToday(null, mCurrentCard, false);
//        Utils.updateProgressBars(mSessionProgressBar,
//                (int) (mStatisticBarsMax * progress[0]), mStatisticBarsHeight);
//        Utils.updateProgressBars(mSessionProgressTotalBar,
//                (int) (mStatisticBarsMax * progress[1]), mStatisticBarsHeight);
//    }
}

