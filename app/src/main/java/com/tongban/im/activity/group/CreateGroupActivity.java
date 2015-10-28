package com.tongban.im.activity.group;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.tb.api.FileUploadApi;
import com.tb.api.GroupApi;
import com.tb.api.callback.UploadFileCallback;
import com.tb.api.model.BaseEvent;
import com.tb.api.model.ImageUrl;
import com.tb.api.model.group.GroupType;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.corelib.widget.view.CircleImageView;
import com.tongban.im.R;
import com.tongban.im.activity.base.CommonImageResultActivity;
import com.tongban.im.common.Consts;
import com.tongban.im.utils.LocationUtils;
import com.tongban.im.widget.view.CameraView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建圈子界面
 *
 * @author fushudi
 */
public class CreateGroupActivity extends CommonImageResultActivity implements
        CommonImageResultActivity.IPhotoListener {

    //选择位置
    public static int SELECT_LOCATION = 310;
    //选择标签
    public static int SELECT_LABEL = 320;

    @Bind(R.id.iv_group_portrait)
    CircleImageView ivGroupPortrait;
    @Bind(R.id.et_group_name)
    EditText etGroupName;
    @Bind(R.id.tv_child_age)
    TextView tvBirthday;
    @Bind(R.id.v_child_age)
    View mChildAge;
    @Bind(R.id.tv_group_label)
    TextView tvGroupLabel;
    @Bind(R.id.tv_group_location)
    TextView tvLocation;
    @Bind(R.id.et_group_desc)
    EditText etDesc;
    @Bind(R.id.chb_secret)
    CheckBox chbSecret;
    @Bind(R.id.chb_agreement)
    CheckBox chbAgree;
    @Bind(R.id.btn_create)
    Button btnSubmit;

    private CameraView mCameraView;

    private int mGroupType;
    private String titleName;

    private DatePickerDialog mDatePickerDialog;
    private byte[] mGroupBytes;
    /**
     * 经纬度
     */
    private double longitude, latitude;
    /**
     * 位置信息
     */
    private String province, city, county, address, birthday, tags, declaration;
    private ArrayList<String> selectTagId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(titleName.equals("") ? getString(R.string.create_group) :
                getString(R.string.create) + titleName);

    }

    @Override
    protected int getLayoutRes() {
        if (getIntent().getExtras() != null) {
            mGroupType = getIntent().getExtras().getInt(Consts.KEY_GROUP_TYPE, 0);
            titleName = getIntent().getExtras().getString(Consts.KEY_GROUP_TYPE_NAME, "");
        }

        return R.layout.activity_create_group;
    }


    //设置文本的可见度
    private void setTextVisible() {
        switch (mGroupType) {
            case GroupType.AGE:
                tvBirthday.setVisibility(View.VISIBLE);
                mChildAge.setVisibility(View.VISIBLE);
                break;
            case GroupType.LIFE:
//                tvLife.setVisibility(View.VISIBLE);
//                mLife.setVisibility(View.VISIBLE);
                break;
            case GroupType.CLASSMATE:
                tvLocation.setHint(R.string.create_school);
                break;
        }
    }

    @Override
    protected void initData() {
        LocationUtils.get(mContext).start();
        setTextVisible();
        setmPhotoListener(this);

    }

    @OnClick({R.id.iv_group_portrait, R.id.btn_create, R.id.tv_group_location
            , R.id.tv_group_label, R.id.tv_child_age})
    public void onClick(View v) {
        if (v == ivGroupPortrait) {
            createDialog();
        }
        //发表圈子
        else if (v == btnSubmit) {
            final String groupName = etGroupName.getText().toString().trim();
            if (TextUtils.isEmpty(groupName)) {
                ToastUtil.getInstance(mContext).showToast("请输入圈子名称");
                return;
            }
            if (!chbAgree.isChecked()) {
                ToastUtil.getInstance(mContext).showToast("请勾选'" + getString(R.string.group_agreement) + "'");
                return;
            }

            if (TextUtils.isEmpty(address)) {
                ToastUtil.getInstance(mContext).showToast("请选择一个位置");
                return;
            }
            declaration = etDesc.getText().toString().trim();
            //不选头像，将不上传
            if (mGroupBytes == null) {
                GroupApi.getInstance().createGroup(groupName, mGroupType, longitude,
                        latitude, address,
                        birthday, tags, declaration, null, chbSecret.isChecked(),
                        CreateGroupActivity.this);
            }
            //上传头像
            else {
                showProgress();
                FileUploadApi.getInstance().uploadFile(mGroupBytes, null, FileUploadApi.IMAGE_SIZE_300,
                        FileUploadApi.IMAGE_SIZE_500, new UploadFileCallback() {

                            @Override
                            public void uploadSuccess(ImageUrl url) {
                                GroupApi.getInstance().createGroup(groupName, mGroupType, longitude,
                                        latitude, address,
                                        birthday, tags, declaration, url, chbSecret.isChecked(),
                                        CreateGroupActivity.this);
                            }

                            @Override
                            public void uploadFailed(String error) {
                                hideProgress();
                                ToastUtil.getInstance(mContext).showToast("创建失败,请重试");
                            }

                        });
            }

        } else if (v == tvLocation) {
            Intent intent = new Intent(mContext, SearchPoiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Consts.KEY_GROUP_TYPE, mGroupType);
            bundle.putString(Consts.KEY_SELECTED_POI_NAME, address);
            intent.putExtras(bundle);
            startActivityForResult(intent, SELECT_LOCATION);
        } else if (v == tvGroupLabel) {
            Intent intent = new Intent(mContext, GroupTipsListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Consts.KEY_GROUP_TYPE, mGroupType);
            bundle.putStringArrayList("selectTag", selectTagId);
            intent.putExtras(bundle);
            startActivityForResult(intent, SELECT_LABEL);
        } else if (v == tvBirthday) {
            openDatePicker();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK != resultCode) {
            return;
        }
        if (requestCode == SELECT_LOCATION) {
            longitude = data.getDoubleExtra(Consts.LONGITUDE, Consts.DEFAULT_DOUBLE);
            latitude = data.getDoubleExtra(Consts.LATITUDE, Consts.DEFAULT_DOUBLE);
            address = data.getStringExtra(Consts.KEY_SELECTED_POI_NAME);
            tvLocation.setText(address);

            setLocationInfo();
        }
    }

    // 打开相机的提示框
    private void createDialog() {
        if (mCameraView == null) {
            mCameraView = new CameraView(this);
        }
        mCameraView.show();
    }

    //打开时间选择器
    private void openDatePicker() {
        if (mDatePickerDialog == null) {
            Calendar c = Calendar.getInstance();
            mDatePickerDialog = new DatePickerDialog(mContext,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String month = String.valueOf(monthOfYear + 1);
                            String day = String.valueOf(dayOfMonth);

                            if (month.length() == 1) month = "0" + month;
                            if (day.length() == 1) day = "0" + day;

                            birthday = year + "-" + month + "-" + day;
                            tvBirthday.setText(birthday);
                        }
                    }, c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH));
        }
        Calendar max = Calendar.getInstance();
        max.add(Calendar.YEAR, 0);
        max.add(Calendar.MONTH, 0);
        max.add(Calendar.DAY_OF_MONTH, 0);
        mDatePickerDialog.getDatePicker().setMaxDate(max.getTime().getTime());
        mDatePickerDialog.show();
    }

    //圈子创建成功Event
    public void onEventMainThread(BaseEvent.CreateGroupEvent obj) {
        // 创建成功
        ToastUtil.getInstance(mContext).showToast("创建成功");
        finish();
    }

    //标签选择完成Event
    public void onEventMainThread(BaseEvent.LabelEvent obj) {
        tags = "";
        selectTagId.clear();
        selectTagId.addAll(obj.label);
        for (int i = 0; i < selectTagId.size(); i++) {
            tags += selectTagId.get(i);
            if ((i + 1) < selectTagId.size())
                tags += ",";
        }
        tvGroupLabel.setText(tags);
    }

    public void onEventMainThread(BDLocation obj) {
        longitude = (Double) SPUtils.get(mContext, Consts.LONGITUDE, Consts.DEFAULT_DOUBLE);
        latitude = (Double) SPUtils.get(mContext, Consts.LATITUDE, Consts.DEFAULT_DOUBLE);
        province = (String) SPUtils.get(mContext, Consts.PROVINCE, "");
        city = (String) SPUtils.get(mContext, Consts.CITY, "");
        county = (String) SPUtils.get(mContext, Consts.COUNTY, "");
        address = (String) SPUtils.get(mContext, Consts.ADDRESS, "");
        setLocationInfo();
    }


    //拼接当前位置，用于接口接收[省，市，区，详细地址]
    private void setLocationInfo() {
        address = province + "," + city + "," + county + "," + address;
    }

    @Override
    public void sendPhoto(byte[] bytes) {
        mGroupBytes = bytes;
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        ivGroupPortrait.setImageBitmap(bitmap);
    }
}
