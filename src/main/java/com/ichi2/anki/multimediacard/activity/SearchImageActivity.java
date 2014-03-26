/****************************************************************************************
 * Copyright (c) 2013 Bibek Shrestha <bibekshrestha@gmail.com>                          *
 * Copyright (c) 2013 Zaur Molotnikov <qutorial@gmail.com>                              *
 * Copyright (c) 2013 Nicolas Raoul <nicolas.raoul@gmail.com>                           *
 * Copyright (c) 2013 Flavio Lerda <flerda@gmail.com>                                   *
 *                                                                                      *
 * This program is free software; you can redistribute it and/or modify it under        *
 * the terms of the GNU General Public License as published by the Free Software        *
 * Foundation; either version 3 of the License, or (at your option) any later           *
 * version.                                                                             *
 *                                                                                      *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY      *
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A      *
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.             *
 *                                                                                      *
 * You should have received a copy of the GNU General Public License along with         *
 * this program.  If not, see <http://www.gnu.org/licenses/>.                           *
 ****************************************************************************************/

package com.ichi2.anki.multimediacard.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;

import com.google.gson.Gson;
import com.ichi2.anki.R;
import com.ichi2.anki.multimediacard.googleimagesearch.json.BResult;
import com.ichi2.anki.multimediacard.googleimagesearch.json.ImageSearchResponse;
import com.ichi2.anki.multimediacard.googleimagesearch.json.ResponseData;
import com.ichi2.anki.multimediacard.googleimagesearch.json.Result;
import com.ichi2.anki.web.HttpFetcher;
import com.ichi2.anki.web.UrlTools;
import com.ichi2.async.DeckTask;
import com.ichi2.widget.ScaleImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SearchImageActivity extends Activity implements DialogInterface.OnCancelListener {
    private static final String BUNDLE_KEY_SHUT_OFF = "key.multimedia.shut.off";

    public static final String EXTRA_SOURCE = "search.image.activity.extra.source";
    // Passed out as a result
    public static String EXTRA_IMAGE_FILE_PATH = "com.ichi2.anki.search.image.activity.extra.image.file.path";
    private GridView listView;
    private ImageAdapter mAdapter = new ImageAdapter();
    private String mSource;
    private Button mPrevButton;
    private Button mNextButton;
    private ProgressDialog progressDialog;

    private List<BResult> results = new ArrayList<BResult>();
    private int mCurrentImage;
    private String mTemplate = null;
    private Button mPickButton;
    private DownloadFileTask mDownloadMp3Task;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(BUNDLE_KEY_SHUT_OFF, true);

    }


    private void finishCancel() {
        Intent resultData = new Intent();
        setResult(RESULT_CANCELED, resultData);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            boolean b = savedInstanceState.getBoolean(BUNDLE_KEY_SHUT_OFF, false);
            if (b) {
                finishCancel();
                return;
            }
        }

        setContentView(R.layout.activity_search_image);

        try {
            mSource = getIntent().getExtras().getString(EXTRA_SOURCE).toString();
        } catch (Exception e) {
            mSource = "";
        }

        listView = (GridView) findViewById(R.id.gridview);
        ((GridView) listView).setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pickImage(position);
            }
        });



        mNextButton = (Button) findViewById(R.id.ImageSearchNext);

        mNextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                nextClicked();
            }
        });

        mPrevButton = (Button) findViewById(R.id.ImageSearchPrev);

        mPrevButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                prevClicked();
            }
        });

        mPrevButton.setEnabled(false);

    }

    /**
     * @author zaur This is to load finally the MP3 file with pronunciation.
     */
    private class DownloadFileTask extends AsyncTask<Void, Void, String> {

        private String mAddress;
        private Context mActivity;


        @Override
        protected String doInBackground(Void... params) {
            return HttpFetcher.downloadFileToSdCard(mAddress, mActivity, "imgsearch");
        }


        public void setAddress(String address) {
            mAddress = address;
        }


        @Override
        protected void onPostExecute(String result) {
            receiveImageFile(result);
        }


        public void setActivity(Context mActivity) {
            this.mActivity = mActivity;
        }

    }


    protected void pickImage(int currentImage) {
        String imageUrl = results.get(currentImage).getObjURL();

        // And here it is possible to download it... so on,
        // then return file path.

        // Download MP3 file
        try {
            progressDialog = ProgressDialog.show(this, gtxt(R.string.multimedia_editor_progress_wait_title),
                    gtxt(R.string.multimedia_editor_imgs_saving_image), true, false);
            mDownloadMp3Task = new DownloadFileTask();
            mDownloadMp3Task.setActivity(this);
            mDownloadMp3Task.setAddress(imageUrl);
            mDownloadMp3Task.execute();
        } catch (Exception e) {
            progressDialog.dismiss();
            returnFailure(gtxt(R.string.multimedia_editor_something_wrong));
        }
    }


    public void receiveImageFile(String result) {
        dismissCarefullyProgressDialog();

        Intent resultData = new Intent();

        resultData.putExtra(EXTRA_IMAGE_FILE_PATH, result);

        setResult(RESULT_OK, resultData);

        finish();

    }


    private class BackgroundPost extends AsyncTask<Void, Void, ImageSearchResponse> {

        private String mQuery;


        @Override
        protected ImageSearchResponse doInBackground(Void... params) {
            try {

                URL url = new URL("http://image.baidu.com/i?tn=baiduimagejson&word=Q&rn=15&pn=N"
                        .replaceAll("Q", getQuery()).replaceAll("N", mCurrentImage*15 + ""));
                URLConnection connection = url.openConnection();
                connection.addRequestProperty("Referer", "anki.ichi2.com");

                String line;
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                Gson gson = new Gson();
                ImageSearchResponse resp = gson.fromJson(builder.toString(), ImageSearchResponse.class);

                resp.setOk(true);
                return resp;

            } catch (Exception e) {
                return new ImageSearchResponse();
            }
        }


        @Override
        protected void onPostExecute(ImageSearchResponse result) {
            postFinished(result);
        }


        /**
         * @param query Used to set the download address
         */
        public void setQuery(String query) {
            mQuery = query;
        }


        /**
         * @return Used to know, which of the posts finished, to differentiate.
         */
        public String getQuery() {
            return UrlTools.encodeUrl(mQuery);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        progressDialog = ProgressDialog.show(this, getText(R.string.multimedia_editor_progress_wait_title),
                getText(R.string.multimedia_editor_imgs_searching_for_images), true, false);

        progressDialog.setCancelable(true);
        progressDialog.setOnCancelListener(this);

        BackgroundPost p = new BackgroundPost();
        p.setQuery(mSource);
        p.execute();
    }


    public void postFinished(ImageSearchResponse response) {
        results.clear();
        ArrayList<String> theImages = new ArrayList<String>();

        // No loop, just a good construct to break out from
        do {
            if (response == null)
                break;

            if (response.getOk() == false)
                break;



            List<BResult> resultList = response.getData();

            if (resultList == null)
                break;

            results.addAll(resultList);



            if (results.size() == 0)
                break;

            dismissCarefullyProgressDialog();

            mAdapter.notifyDataSetChanged();

            return;

        } while (false);

        returnFailure(gtxt(R.string.multimedia_editor_imgs_no_results));
    }

    private void showCurrentImage() {
        if (mCurrentImage <= 0) {
            mCurrentImage = 0;
            mPrevButton.setEnabled(false);
        }

        if (mCurrentImage > 0) {
            mCurrentImage = Math.min(results.size() - 1, mCurrentImage);
            mPrevButton.setEnabled(true);
        }

        BackgroundPost p = new BackgroundPost();
        p.setQuery(mSource);
        p.execute();
    }


    private void returnFailure(String explanation) {
        showToast(explanation);
        setResult(RESULT_CANCELED);
        dismissCarefullyProgressDialog();
        finish();
    }


    private void dismissCarefullyProgressDialog() {
        try {
            if (progressDialog != null) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        } catch (Exception e) {
            // nothing is done intentionally
        }
    }


    private void showToast(CharSequence text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }


    protected void prevClicked() {
        --mCurrentImage;
        showCurrentImage();

    }


    protected void nextClicked() {
        ++mCurrentImage;
        showCurrentImage();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_search_image, menu);
        return true;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // nothing
    }


    private String gtxt(int id) {
        return getText(id).toString();
    }

    public class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return results.size();
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

            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
                convertView = layoutInflator.inflate(R.layout.quiz_grid_image, null);
                holder = new ViewHolder();
                holder.imageView = (ScaleImageView) convertView.findViewById(R.id.img_thumb);
                holder.contentView = (TextView) convertView.findViewById(R.id.img_size);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ImageLoader.getInstance().displayImage(results.get(position).getThumbURL(), holder.imageView);
            holder.contentView.setText(results.get(position).getFilesize() + "K");

            return convertView;
        }

        class ViewHolder {
            public ScaleImageView imageView;
            TextView contentView;
        }
    }
}

