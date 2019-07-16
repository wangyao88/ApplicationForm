package cn.stylefeng.guns.modular.business.entity;


import java.util.Comparator;

public class ApplicationDetailComparator implements Comparator<ApplicationDetail> {

    @Override
    public int compare(ApplicationDetail o1, ApplicationDetail o2) {
        Integer thisDateNum = o1.getDateNum();
        Integer otherDateNum = o1.getDateNum();
        return thisDateNum.compareTo(otherDateNum);
    }
}
