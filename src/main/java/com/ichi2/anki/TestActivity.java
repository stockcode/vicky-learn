package com.ichi2.anki;

import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.ichi2.libanki.Card;
import com.ichi2.libanki.Collection;
import com.ichi2.libanki.Note;
import com.ichi2.themes.Themes;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by gengke on 14-3-19.
 */
public class TestActivity extends AnkiActivity {

    private GridView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Themes.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        // The hardware buttons should control the music volume while reviewing.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Collection col = AnkiDroidApp.getCol();

        Card card = col.getSched().getCard();
        Note note = col.getNote(0);

        Log.e("test", note.toString());

        //Bundle bundle = getIntent().getExtras();
        //imageUrls = bundle.getStringArray(Extra.IMAGES);



        listView = (GridView) findViewById(R.id.gridview);
        ((GridView) listView).setAdapter(new ImageAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startImagePagerActivity(position);
            }
        });
    }

    public class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 10;
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

            //ImageLoader.displayImage((imageUrls[position], holder.imageView);

            return view;
        }

        class ViewHolder {
            ImageView imageView;
        }
    }
}

