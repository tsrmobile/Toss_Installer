package th.co.thiensurat.toss_installer.utils.helper;

import java.util.List;

import th.co.thiensurat.toss_installer.job.item.JobItem;

/**
 * Created by teerayut.k on 11/10/2017.
 */

public interface OnCustomerListChangedListener {
    void onNoteListChanged(List<JobItem> jobItemList);
}
