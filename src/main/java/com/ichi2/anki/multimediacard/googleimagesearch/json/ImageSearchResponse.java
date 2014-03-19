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

package com.ichi2.anki.multimediacard.googleimagesearch.json;

import java.util.List;

public class ImageSearchResponse {
    private String queryEnc;
    private String queryExt;
    private Number listNum;
    private Number displayNum;
    private String bdFmtDispNum;
    private String bdSearchTime;
    private String bdIsClustered;
    private List<BResult> data;
    private Boolean ok = false;

    public Boolean getOk() {
        return ok;
    }

    public void setOk(Boolean ok) {
        this.ok = ok;
    }

    public String getQueryEnc() {
        return queryEnc;
    }

    public void setQueryEnc(String queryEnc) {
        this.queryEnc = queryEnc;
    }

    public String getQueryExt() {
        return queryExt;
    }

    public void setQueryExt(String queryExt) {
        this.queryExt = queryExt;
    }

    public Number getListNum() {
        return listNum;
    }

    public void setListNum(Number listNum) {
        this.listNum = listNum;
    }

    public Number getDisplayNum() {
        return displayNum;
    }

    public void setDisplayNum(Number displayNum) {
        this.displayNum = displayNum;
    }

    public String getBdFmtDispNum() {
        return bdFmtDispNum;
    }

    public void setBdFmtDispNum(String bdFmtDispNum) {
        this.bdFmtDispNum = bdFmtDispNum;
    }

    public String getBdSearchTime() {
        return bdSearchTime;
    }

    public void setBdSearchTime(String bdSearchTime) {
        this.bdSearchTime = bdSearchTime;
    }

    public String getBdIsClustered() {
        return bdIsClustered;
    }

    public void setBdIsClustered(String bdIsClustered) {
        this.bdIsClustered = bdIsClustered;
    }

    public List<BResult> getData() {
        return data;
    }

    public void setData(List<BResult> data) {
        this.data = data;
    }
}
