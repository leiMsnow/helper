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
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.tongban.corelib.utils.SPUtils;
import com.tongban.corelib.utils.ToastUtil;
import com.tongban.im.R;
import com.tongban.im.activity.base.CameraResultActivity;
import com.tongban.im.api.FileUploadApi;
import com.tongban.im.api.GroupApi;
import com.tongban.im.api.UploadFileCallback;
import com.tongban.im.common.Consts;
import com.tongban.im.model.BaseEvent;
import com.tongban.im.model.GroupType;
import com.tongban.im.model.ImageUrl;
import com.tongban.im.utils.CameraUtils;
import com.tongban.im.utils.LocationUtils;
import com.tongban.im.widget.view.CameraView;

import java.util.Calendar;

/**
 * 创建圈子界面
 *
 * @author fushudi
 */
public class CreateGroupActivity extends CameraResultActivity implements View.OnClickListener,
        CameraResultActivity.IPhotoListener {

    //选择位置
    public static int SELECT_LOCATION = 310;
    //选择标签
    public static int SELECT_LABEL = 320;

    private ImageView ivSetGroupIcon;
    private EditText etGroupName, etDesc;
    private TextView tvGroupLabel, tvLocation, tvBirthday, tvLife;
    private CheckBox chbSecret, chbAgree;
    private Button btnSubmit;

    private CameraView mCameraView;

    private int mGroupType;
    private String titleName;
    private int mGroupIcon;

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
            mGroupIcon = getIntent().getExtras().getInt(Consts.KEY_GROUP_TYPE_ICON,
                    R.mipmap.ic_group_create);
            setToolbarTheme(mGroupType);
        }

        return R.layout.activity_create_group;
    }

    @Override
    protected void initView() {

        ivSetGroupIcon = (ImageView) findViewById(R.id.iv_group_portrait);
        etGroupName = (EditText) findViewById(R.id.et_group_name);
        etGroupName.requestFocus();
        etDesc = (EditText) findViewById(R.id.et_group_desc);

        tvGroupLabel = (TextView) findViewById(R.id.tv_group_label);
        tvLocation = (TextView) findViewById(R.id.tv_group_location);

        tvBirthday = (TextView) findViewById(R.id.tv_child_age);
        tvLife = (TextView) findViewById(R.id.tv_life);

        chbSecret = (CheckBox) findViewById(R.id.chb_secret);
        chbAgree = (CheckBox) findViewById(R.id.chb_agreement);

        btnSubmit = (Button) findViewById(R.id.btn_create);

        setTextVisible();
    }

    //设置文本的可见度
    private void setTextVisible() {
        switch (mGroupType) {
            case GroupType.AGE:
                tvBirthday.setVisibility(View.VISIBLE);
                break;
            case GroupType.LIFE:
//                tvLife.setVisibility(View.VISIBLE);
                break;
            case GroupType.CLASSMATE:
                tvLocation.setHint(R.string.create_school);
                break;
        }
    }

    @Override
    protected void initListener() {

        setmPhotoListener(this);

        ivSetGroupIcon.setOnClickListener(this);

        tvLocation.setOnClickListener(this);
        tvGroupLabel.setOnClickListener(this);
        tvBirthday.setOnClickListener(this);
        tvLife.setOnClickListener(this);

        btnSubmit.setOnClickListener(this);


    }

    @Override
    protected void initData() {
        LocationUtils.get(mContext).start();
        ivSetGroupIcon.setImageResource(mGroupIcon);
    }

    @Override
    public void onClick(View v) {
        if (v == ivSetGroupIcon) {
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
            if (mGroupBytes == null) {
                Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), mGroupIcon);
                mGroupBytes = CameraUtils.Bitmap2Bytes(bitmap);
            }
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
                            GroupApi.getInstance().createGroup(groupName, mGroupType, longitude,
                                    latitude, address,
                                    birthday, tags, declaration, null, chbSecret.isChecked(),
                                    CreateGroupActivity.this);
                        }

                    });


        } else if (v == tvLocation) {
            Intent intent = new Intent(mContext, SearchPoiActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Consts.KEY_GROUP_TYPE, mGroupType);
            bundle.putString(Consts.KEY_SELECTED_POI_NAME, address);
            intent.putExtras(bundle);
            startActivityForResult(intent, SELECT_LOCATION);
        } else if (v == tvGroupLabel) {
            Intent intent = new Intent(mContext, LabelListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(Consts.KEY_GROUP_TYPE, mGroupType);
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
        tags = obj.label;
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
        ivSetGroupIcon.setImageBitmap(bitmap);
    }
}
