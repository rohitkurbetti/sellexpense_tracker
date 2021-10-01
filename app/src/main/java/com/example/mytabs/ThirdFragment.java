package com.example.mytabs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.print.PrintManager;
import android.text.InputType;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.Leading;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import org.intellij.lang.annotations.JdkConstants;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ThirdFragment extends Fragment {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private long longDate;
    private long toPutMaxDate;
    TextView fromDate;
    TextView toDate;
    String toDateFmt;
    String fromDateFmt;
    Button editData;
    EditText orangeu;
    EditText kokamu;
    EditText lemonu;
    EditText sarbatu;
    EditText ssrbtu;
    EditText ssodau;
    EditText pachaku;
    EditText walau;
    EditText lsodau;
    EditText jsodau;
    EditText lorangeu;
    EditText llemonu;
    EditText wateru;
    EditText lassiu;
    EditText otheru;
    EditText vanillau;
    EditText pistau;
    EditText mangou;
    EditText btrschu;
    EditText stwbryu;
    EditText kulfiu;
    EditText cbaru;
    EditText fpacku;
    EditText other1u;
    EditText editBillNo;
    EditText btnCommit;
    EditText etcName;
    EditText etDelBillNo;
    Button btnDelBillNo;
    TableLayout t;
    int billno;
    int grandTotal=0;
    Cursor res;
    String customerName;
    TextView currDate1,tvTotal,srno1;
    int vanillau1,pistau1,stwbryu1,btrschu1,kulfiu1,cbaru1,fpacku1,otheru1,other1u1,mangou1,orangeu1,kokamu1,sarbatu1,lemonu1,lsodau1,ssrbtu1,walau1,wateru1,lassiu1,pachaku1,lorangeu1,llemonu1,jsodau1,ssodau1;
    private DbManager db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        t = view.findViewById(R.id.tbEstimation);
        db = new DbManager(getContext());
        ((MainActivity) getActivity()).f3 = this;
        prefs = getLayoutInflater().getContext().getSharedPreferences("d",MODE_PRIVATE);
        editor = prefs.edit();
        Spinner spinner = view.findViewById(R.id.spinner);
        fromDate = view.findViewById(R.id.fromDate);
        toDate = view.findViewById(R.id.toDate);
        Button btnVew = view.findViewById(R.id.btnView);
        editData = view.findViewById(R.id.editData);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            editData.setTooltipText("Edit Record");
        }
        editBillNo = view.findViewById(R.id.editBillNo);
        btnDelBillNo = view.findViewById(R.id.btnDelBillNo);
        etDelBillNo = view.findViewById(R.id.etBillNo);
        currDate1 = view.findViewById(R.id.currDate);
        tvTotal = view.findViewById(R.id.total1);
        srno1 = view.findViewById(R.id.serialNo);
        ImageButton imgBtn = view.findViewById(R.id.imgBtn);
        EditText billName = view.findViewById(R.id.txtField);
        List<String> items = new ArrayList<>();
        items.add("Date Wise");
        items.add("Name Wise");
        items.add("Bill Wise");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        fillTable(view);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    doPrintReport();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        btnDelBillNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etDelBillNo.getText().toString().isEmpty() && !etDelBillNo.getText().toString().equals("0")){
                    int billNo = Integer.parseInt(etDelBillNo.getText().toString());
                    res = db.getOne(billNo);
                    if(res.getCount() > 0){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Delete BillNo "+billNo+"?");
                        builder.setMessage("Are you sure you want to delete BillNo");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String responseMsg = db.deleteByBillNo(billNo);
                                Toast.makeText(getContext(), responseMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        etDelBillNo.setText("");
                    }else{
                        Toast.makeText(getContext(), "No Bill Found", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Please enter BillNo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editBillNo.getText().toString().isEmpty() && !editBillNo.getText().equals(null)) {
                    res = db.getOne(Integer.parseInt(String.valueOf(editBillNo.getText())));
                    if (res.getCount() > 0) {
                        final View custEditView = getLayoutInflater().inflate(R.layout.edit_view, null);
                        etcName = custEditView.findViewById(R.id.etcname);
                        orangeu = custEditView.findViewById(R.id.orangeu);
                        kokamu = custEditView.findViewById(R.id.kokamu);
                        lemonu = custEditView.findViewById(R.id.lemonu);
                        ssrbtu = custEditView.findViewById(R.id.ssrbtu);
                        sarbatu = custEditView.findViewById(R.id.sarbatu);
                        walau = custEditView.findViewById(R.id.walau);
                        pachaku = custEditView.findViewById(R.id.pachaku);
                        lsodau = custEditView.findViewById(R.id.lsodau);
                        ssodau = custEditView.findViewById(R.id.ssodau);
                        lassiu = custEditView.findViewById(R.id.lassiu);
                        jsodau = custEditView.findViewById(R.id.jsodau);
                        wateru = custEditView.findViewById(R.id.wateru);
                        lorangeu = custEditView.findViewById(R.id.lorangeu);
                        llemonu = custEditView.findViewById(R.id.llemonu);
                        otheru = custEditView.findViewById(R.id.otheru);

                        vanillau = custEditView.findViewById(R.id.vanillau);
                        pistau = custEditView.findViewById(R.id.pistau);
                        mangou = custEditView.findViewById(R.id.mangou);
                        btrschu = custEditView.findViewById(R.id.btrschu);
                        stwbryu = custEditView.findViewById(R.id.stwbryu);
                        kulfiu = custEditView.findViewById(R.id.kulfiu);
                        cbaru = custEditView.findViewById(R.id.cbaru);
                        fpacku = custEditView.findViewById(R.id.fpacku);
                        other1u = custEditView.findViewById(R.id.other1u);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        builder.setView(custEditView);

                        while (res.moveToNext()) {
                            builder.setTitle("Editing BillNo:" + res.getInt(1));
                            billno = res.getInt(1);
                            customerName = res.getString(2);
                            orangeu1 = res.getInt(3);
                            kokamu1 = res.getInt(4);
                            lemonu1 = res.getInt(5);
                            sarbatu1 = res.getInt(6);
                            pachaku1 = res.getInt(7);
                            walau1 = res.getInt(8);
                            lsodau1 = res.getInt(9);
                            ssrbtu1 = res.getInt(10);
                            lorangeu1 = res.getInt(11);
                            llemonu1 = res.getInt(12);
                            jsodau1 = res.getInt(13);
                            ssodau1 = res.getInt(14);
                            wateru1 = res.getInt(15);
                            lassiu1 = res.getInt(16);

                            vanillau1 = res.getInt(17);
                            pistau1 = res.getInt(18);
                            stwbryu1 = res.getInt(19);
                            mangou1 = res.getInt(20);
                            btrschu1 = res.getInt(21);
                            kulfiu1 = res.getInt(22);
                            cbaru1 = res.getInt(23);
                            fpacku1 = res.getInt(24);
                            otheru1 = res.getInt(25);
                            other1u1 = res.getInt(26);

                        }
                        etcName.setText(String.valueOf(customerName));
                        orangeu.setText(String.valueOf(orangeu1));
                        lemonu.setText(String.valueOf(lemonu1));
                        kokamu.setText(String.valueOf(kokamu1));
                        sarbatu.setText(String.valueOf(sarbatu1));
                        ssrbtu.setText(String.valueOf(ssrbtu1));
                        walau.setText(String.valueOf(walau1));
                        pachaku.setText(String.valueOf(pachaku1));
                        lorangeu.setText(String.valueOf(lorangeu1));
                        llemonu.setText(String.valueOf(llemonu1));
                        jsodau.setText(String.valueOf(jsodau1));
                        ssodau.setText(String.valueOf(ssodau1));
                        wateru.setText(String.valueOf(wateru1));
                        lassiu.setText(String.valueOf(lassiu1));
                        lsodau.setText(String.valueOf(lsodau1));
                        otheru.setText(String.valueOf(otheru1));

                        vanillau.setText(String.valueOf(vanillau1));
                        pistau.setText(String.valueOf(pistau1));
                        stwbryu.setText(String.valueOf(stwbryu1));
                        mangou.setText(String.valueOf(mangou1));
                        btrschu.setText(String.valueOf(btrschu1));
                        kulfiu.setText(String.valueOf(kulfiu1));
                        cbaru.setText(String.valueOf(cbaru1));
                        fpacku.setText(String.valueOf(fpacku1));
                        other1u.setText(String.valueOf(other1u1));



                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(orangeu.getText().toString().equals("")){
                                    orangeu.setText(String.valueOf("0"));
                                }
                                if(lemonu.getText().toString().equals("")){
                                    lemonu.setText(String.valueOf("0"));
                                }
                                if(kokamu.getText().toString().equals("")){
                                    kokamu.setText(String.valueOf("0"));
                                }
                                if(walau.getText().toString().equals("")){
                                    walau.setText(String.valueOf("0"));
                                }
                                if(lassiu.getText().toString().equals("")){
                                    lassiu.setText(String.valueOf("0"));
                                }
                                if(lsodau.getText().toString().equals("")){
                                    lsodau.setText(String.valueOf("0"));
                                }
                                if(pachaku.getText().toString().equals("")){
                                    pachaku.setText(String.valueOf("0"));
                                }
                                if(lsodau.getText().toString().equals("")){
                                    lsodau.setText(String.valueOf("0"));
                                }
                                if(ssrbtu.getText().toString().equals("")){
                                    ssrbtu.setText(String.valueOf("0"));
                                }
                                if(ssodau.getText().toString().equals("")){
                                    ssodau.setText(String.valueOf("0"));
                                }
                                if(jsodau.getText().toString().equals("")){
                                    jsodau.setText(String.valueOf("0"));
                                }
                                if(wateru.getText().toString().equals("")){
                                    wateru.setText(String.valueOf("0"));
                                }
                                if(lorangeu.getText().toString().equals("")){
                                    lorangeu.setText(String.valueOf("0"));
                                }
                                if(llemonu.getText().toString().equals("")){
                                    llemonu.setText(String.valueOf("0"));
                                }
                                if(sarbatu.getText().toString().equals("")){
                                    sarbatu.setText(String.valueOf("0"));
                                }
                                if(otheru.getText().toString().equals("")){
                                    otheru.setText(String.valueOf("0"));
                                }

                                //

                                if(vanillau.getText().toString().equals("")){
                                    vanillau.setText(String.valueOf("0"));
                                }
                                if(pistau.getText().toString().equals("")){
                                    pistau.setText(String.valueOf("0"));
                                }

                                if(stwbryu.getText().toString().equals("")){
                                    stwbryu.setText(String.valueOf("0"));
                                }

                                if(mangou.getText().toString().equals("")){
                                    mangou.setText(String.valueOf("0"));
                                }

                                if(btrschu.getText().toString().equals("")){
                                    btrschu.setText(String.valueOf("0"));
                                }

                                if(kulfiu.getText().toString().equals("")){
                                    kulfiu.setText(String.valueOf("0"));
                                }

                                if(cbaru.getText().toString().equals("")){
                                    cbaru.setText(String.valueOf("0"));
                                }
                                if(fpacku.getText().toString().equals("")){
                                    fpacku.setText(String.valueOf("0"));
                                }

                                if(other1u.getText().toString().equals("")){
                                    other1u.setText(String.valueOf("0"));
                                }

                                grandTotal = grandTotal + Integer.parseInt(orangeu.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(kokamu.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(lemonu.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(ssrbtu.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(sarbatu.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(walau.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(pachaku.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(lsodau.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(lorangeu.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(llemonu.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(wateru.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(lassiu.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(jsodau.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(ssodau.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(otheru.getText().toString());

                                //

                                grandTotal = grandTotal + Integer.parseInt(vanillau.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(pistau.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(stwbryu.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(mangou.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(btrschu.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(kulfiu.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(cbaru.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(fpacku.getText().toString());
                                grandTotal = grandTotal + Integer.parseInt(other1u.getText().toString());




                                builder.setMessage("Total:"+grandTotal);
                                Toast.makeText(getContext(), "Total:"+grandTotal, Toast.LENGTH_SHORT).show();
                                db.updateOne(
                                        String.valueOf(etcName.getText().toString()),
                                        Integer.parseInt(orangeu.getText().toString()),
                                        Integer.parseInt(kokamu.getText().toString()),
                                        Integer.parseInt(lemonu.getText().toString()),
                                        Integer.parseInt(sarbatu.getText().toString()),
                                        Integer.parseInt(pachaku.getText().toString()),
                                        Integer.parseInt(walau.getText().toString()),
                                        Integer.parseInt(lsodau.getText().toString()),
                                        Integer.parseInt(ssrbtu.getText().toString()),
                                        Integer.parseInt(lorangeu.getText().toString()),
                                        Integer.parseInt(llemonu.getText().toString()),
                                        Integer.parseInt(jsodau.getText().toString()),
                                        Integer.parseInt(ssodau.getText().toString()),
                                        Integer.parseInt(wateru.getText().toString()),
                                        Integer.parseInt(lassiu.getText().toString()),
                                        Integer.parseInt(vanillau.getText().toString()),
                                        Integer.parseInt(pistau.getText().toString()),
                                        Integer.parseInt(stwbryu.getText().toString()),
                                        Integer.parseInt(mangou.getText().toString()),
                                        Integer.parseInt(btrschu.getText().toString()),
                                        Integer.parseInt(kulfiu.getText().toString()),
                                        Integer.parseInt(cbaru.getText().toString()),
                                        Integer.parseInt(fpacku.getText().toString()),
                                        Integer.parseInt(otheru.getText().toString()),
                                        Integer.parseInt(other1u.getText().toString()),
                                        grandTotal,
                                        billno
                                );
                                Toast.makeText(getContext(), "Record Updated of Bill No:"+billno, Toast.LENGTH_LONG).show();
                                grandTotal=0;
                                editBillNo.setText("");
                                editBillNo.clearFocus();
                                t.removeAllViews();
                                t.refreshDrawableState();
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        Toast.makeText(getContext(), "No record found", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Please Enter Billno", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnVew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cursor res = null;
                final View customView = getLayoutInflater().inflate(R.layout.custom_table_ext, null);
                TableLayout tableLayout = customView.findViewById(R.id.table_layout);
                boolean isDarkThemeOn = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
                TextView tb0 = customView.findViewById(R.id.billno);
                TextView cname = customView.findViewById(R.id.custname);
                TextView tb1 = customView.findViewById(R.id.orange);
                TextView tb2 = customView.findViewById(R.id.kokam);
                TextView tb3 = customView.findViewById(R.id.lemon);
                TextView tb4 = customView.findViewById(R.id.sarbat);
                TextView tb5 = customView.findViewById(R.id.pachak);
                TextView tb6 = customView.findViewById(R.id.wala);
                TextView tb7 = customView.findViewById(R.id.lsoda);
                TextView tb8 = customView.findViewById(R.id.ssrbt);
                TextView tb9 = customView.findViewById(R.id.lorange);
                TextView tb10 = customView.findViewById(R.id.llemon);
                TextView tbJsoda = customView.findViewById(R.id.jsoda);
                TextView tbSsoda = customView.findViewById(R.id.sSoda);
                TextView tbWater = customView.findViewById(R.id.water);
                TextView tbLassi = customView.findViewById(R.id.lassi);
                TextView tb11 = customView.findViewById(R.id.vanilla);
                TextView tb12 = customView.findViewById(R.id.pista);
                TextView tb13 = customView.findViewById(R.id.stwbry);
                TextView tb14 = customView.findViewById(R.id.mango);
                TextView tb15 = customView.findViewById(R.id.btrsch);
                TextView tbKulfi = customView.findViewById(R.id.kulfi);
                TextView tbCbar = customView.findViewById(R.id.cbar);
                TextView tbFpack = customView.findViewById(R.id.fpack);
                TextView tbOther = customView.findViewById(R.id.other);
                TextView tbOther1 = customView.findViewById(R.id.other1);
                TextView tb16 = customView.findViewById(R.id.total);
                TextView tb17 = customView.findViewById(R.id.date1);
                tableLayout.setWeightSum(1);
                if (spinner.getSelectedItem().equals("Date Wise")) {
                    if(fromDate.getText().toString().equals("From date") || toDate.getText().toString().equals("To date")){
                        Toast.makeText(getContext(), "Please select date", Toast.LENGTH_SHORT).show();
                    }else{
                        res = db.findRange(String.valueOf(fromDateFmt) , String.valueOf(toDateFmt));
//                StringBuffer sb=new StringBuffer();
                    int total = 0;
                    while (res.moveToNext()) {
//                    sb.append(res.getInt(1)+"  "+res.getInt(18)+"  "+res.getString(19)+"\n");
                        total += res.getInt(27);
                        TableRow tableRow = new TableRow(customView.getContext());
                        tableRow.setPadding(10, 10, 10, 10);
                        TextView tv1 = new TextView(customView.getContext());
                        tv1.setText(String.valueOf(res.getInt(1)));
                        tv1.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView cname1 = new TextView(customView.getContext());
                        cname1.setText(String.valueOf(res.getString(2)));
                        cname1.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv2 = new TextView(customView.getContext());
                        tv2.setText(String.valueOf(res.getInt(3)));
                        tv2.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv3 = new TextView(customView.getContext());
                        tv3.setText(String.valueOf(res.getInt(4)));
                        tv3.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv4 = new TextView(customView.getContext());
                        tv4.setText(String.valueOf(res.getInt(5)));
                        tv4.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv5 = new TextView(customView.getContext());
                        tv5.setText(String.valueOf(res.getInt(6)));
                        tv5.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv6 = new TextView(customView.getContext());
                        tv6.setText(String.valueOf(res.getInt(7)));
                        tv6.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv7 = new TextView(customView.getContext());
                        tv7.setText(String.valueOf(res.getInt(8)));
                        tv7.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv8 = new TextView(customView.getContext());
                        tv8.setText(String.valueOf(res.getInt(9)));
                        tv8.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv9 = new TextView(customView.getContext());
                        tv9.setText(String.valueOf(res.getInt(10)));
                        tv9.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv10 = new TextView(customView.getContext());
                        tv10.setText(String.valueOf(res.getInt(11)));
                        tv10.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv11 = new TextView(customView.getContext());
                        tv11.setText(String.valueOf(res.getInt(12)));
                        tv11.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvJsoda = new TextView(customView.getContext());
                        tvJsoda.setText(String.valueOf(res.getInt(13)));
                        tvJsoda.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvSsoda = new TextView(customView.getContext());
                        tvSsoda.setText(String.valueOf(res.getInt(14)));
                        tvSsoda.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvWater = new TextView(customView.getContext());
                        tvWater.setText(String.valueOf(res.getInt(15)));
                        tvWater.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvLassi = new TextView(customView.getContext());
                        tvLassi.setText(String.valueOf(res.getInt(16)));
                        tvLassi.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv12 = new TextView(customView.getContext());
                        tv12.setText(String.valueOf(res.getInt(17)));
                        tv12.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv13 = new TextView(customView.getContext());
                        tv13.setText(String.valueOf(res.getInt(18)));
                        tv13.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv14 = new TextView(customView.getContext());
                        tv14.setText(String.valueOf(res.getInt(19)));
                        tv14.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv15 = new TextView(customView.getContext());
                        tv15.setText(String.valueOf(res.getInt(20)));
                        tv15.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv16 = new TextView(customView.getContext());
                        tv16.setText(String.valueOf(res.getInt(21)));
                        tv16.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvKulfi = new TextView(customView.getContext());
                        tvKulfi.setText(String.valueOf(res.getInt(22)));
                        tvKulfi.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvCbar = new TextView(customView.getContext());
                        tvCbar.setText(String.valueOf(res.getInt(23)));
                        tvCbar.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvFpack = new TextView(customView.getContext());
                        tvFpack.setText(String.valueOf(res.getInt(24)));
                        tvFpack.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvOther = new TextView(customView.getContext());
                        tvOther.setText(String.valueOf(res.getInt(25)));
                        tvOther.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tvOther1 = new TextView(customView.getContext());
                        tvOther1.setText(String.valueOf(res.getInt(26)));
                        tvOther1.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv17 = new TextView(customView.getContext());
                        tv17.setText(String.valueOf(res.getInt(27)));
                        tv17.setGravity(Gravity.CENTER_HORIZONTAL);

                        TextView tv18 = new TextView(customView.getContext());
                        tv18.setText(String.valueOf(res.getString(28)));
                        tv18.setGravity(Gravity.CENTER_HORIZONTAL);


                        if (isDarkThemeOn) {
                            cname.setTextColor(Color.WHITE);
                            tb0.setTextColor(Color.WHITE);
                            tb1.setTextColor(Color.WHITE);
                            tb2.setTextColor(Color.WHITE);
                            tb3.setTextColor(Color.WHITE);
                            tb4.setTextColor(Color.WHITE);
                            tb5.setTextColor(Color.WHITE);
                            tb6.setTextColor(Color.WHITE);
                            tb7.setTextColor(Color.WHITE);
                            tb8.setTextColor(Color.WHITE);
                            tb9.setTextColor(Color.WHITE);
                            tb10.setTextColor(Color.WHITE);
                            tb11.setTextColor(Color.WHITE);
                            tb12.setTextColor(Color.WHITE);
                            tb13.setTextColor(Color.WHITE);
                            tb14.setTextColor(Color.WHITE);
                            tb15.setTextColor(Color.WHITE);
                            tb16.setTextColor(Color.WHITE);
                            tb17.setTextColor(Color.WHITE);
                            tbJsoda.setTextColor(Color.WHITE);
                            tbSsoda.setTextColor(Color.WHITE);
                            tbKulfi.setTextColor(Color.WHITE);
                            tbCbar.setTextColor(Color.WHITE);
                            tbFpack.setTextColor(Color.WHITE);
                            tbWater.setTextColor(Color.WHITE);
                            tbLassi.setTextColor(Color.WHITE);
                            tbOther.setTextColor(Color.WHITE);
                            tbOther1.setTextColor(Color.WHITE);
                        } else {
                            cname.setTextColor(Color.BLACK);
                            tb0.setTextColor(Color.BLACK);
                            tb1.setTextColor(Color.BLACK);
                            tb2.setTextColor(Color.BLACK);
                            tb3.setTextColor(Color.BLACK);
                            tb4.setTextColor(Color.BLACK);
                            tb5.setTextColor(Color.BLACK);
                            tb6.setTextColor(Color.BLACK);
                            tb7.setTextColor(Color.BLACK);
                            tb8.setTextColor(Color.BLACK);
                            tb9.setTextColor(Color.BLACK);
                            tb10.setTextColor(Color.BLACK);
                            tb11.setTextColor(Color.BLACK);
                            tb12.setTextColor(Color.BLACK);
                            tb13.setTextColor(Color.BLACK);
                            tb14.setTextColor(Color.BLACK);
                            tb15.setTextColor(Color.BLACK);
                            tb16.setTextColor(Color.BLACK);
                            tb17.setTextColor(Color.BLACK);
                            tbJsoda.setTextColor(Color.BLACK);
                            tbSsoda.setTextColor(Color.BLACK);
                            tbKulfi.setTextColor(Color.BLACK);
                            tbCbar.setTextColor(Color.BLACK);
                            tbFpack.setTextColor(Color.BLACK);
                            tbWater.setTextColor(Color.BLACK);
                            tbLassi.setTextColor(Color.BLACK);
                            tbOther.setTextColor(Color.BLACK);
                            tbOther1.setTextColor(Color.BLACK);
                        }
                        tableRow.addView(tv1);
                        tableRow.addView(cname1);
                        tableRow.addView(tv2);
                        tableRow.addView(tv3);
                        tableRow.addView(tv4);
                        tableRow.addView(tv5);
                        tableRow.addView(tv6);
                        tableRow.addView(tv7);
                        tableRow.addView(tv8);
                        tableRow.addView(tv9);
                        tableRow.addView(tv10);
                        tableRow.addView(tv11);
                        tableRow.addView(tvJsoda);
                        tableRow.addView(tvSsoda);
                        tableRow.addView(tvWater);
                        tableRow.addView(tvLassi);
                        tableRow.addView(tv12);
                        tableRow.addView(tv13);
                        tableRow.addView(tv14);
                        tableRow.addView(tv15);
                        tableRow.addView(tv16);
                        tableRow.addView(tvKulfi);
                        tableRow.addView(tvCbar);
                        tableRow.addView(tvFpack);
                        tableRow.addView(tvOther);
                        tableRow.addView(tvOther1);
                        tableRow.addView(tv17);
                        tableRow.addView(tv18);

//                    }
                        tableLayout.addView(tableRow);
                    }
//                    TextView tvFinal = new TextView(customView.getContext());
//                    tvFinal.setText("\nTotal: "+total);
//                    tvFinal.setPadding(10,10,40,10);
//                    tvFinal.setGravity(Gravity.END);
//                    if(isDarkThemeOn){
//                        tvFinal.setTextColor(Color.WHITE);
//                    }else{
//                        tvFinal.setTextColor(Color.BLACK);
//                    }
//                    tableLayout.addView(tvFinal);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(res.getCount() + " Records Found");
                    String frmdate = fromDate.getText().toString();
                    String todate = toDate.getText().toString();
                    builder.setMessage("\nFrom Date: " + frmdate + "\nTo Date:      " + todate + "\nTotal: " + total);
//                    LayoutInflater layoutInflater = getLayoutInflater();
//                    View img_button = layoutInflater.inflate(R.layout.img_button, null);
//                    builder.setView(img_button);
                    builder.setView(customView);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            doPrint();


                        }

                        private void doPrint() {
//                            PrintManager printManager = (PrintManager)MainActivity.this.getSystemService(Context.PRINT_SERVICE);
                        }
                    });
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    System.out.println(">>>>>" + res.getCount());
                    if (res.getCount() > 0) {
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } else {
                        Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                    }
                } else if (spinner.getSelectedItem().equals("Name Wise")) {
                    billName.requestFocus();
                    if (billName.getText().length() > 0) {
                        res = db.getOneByName(String.valueOf(billName.getText()));
                        if (res.getCount() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            StringBuffer sb = new StringBuffer();
                            builder.setTitle("Record Found");
                            builder.setView(customView);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            while (res.moveToNext()) {
//                                sb.append(res.getInt(1)+"  "+res.getInt(18)+"  "+res.getString(19)+"\n");
                                sb.append("Total: " + res.getInt(27));
                                TableRow tableRow = new TableRow(customView.getContext());
                                tableRow.setPadding(10, 10, 10, 10);
                                TextView tv1 = new TextView(customView.getContext());
                                tv1.setText(String.valueOf(res.getInt(1)));
                                tv1.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView cname1 = new TextView(customView.getContext());
                                cname1.setText(String.valueOf(res.getString(2)));
                                cname1.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv2 = new TextView(customView.getContext());
                                tv2.setText(String.valueOf(res.getInt(3)));
                                tv2.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv3 = new TextView(customView.getContext());
                                tv3.setText(String.valueOf(res.getInt(4)));
                                tv3.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv4 = new TextView(customView.getContext());
                                tv4.setText(String.valueOf(res.getInt(5)));
                                tv4.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv5 = new TextView(customView.getContext());
                                tv5.setText(String.valueOf(res.getInt(6)));
                                tv5.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv6 = new TextView(customView.getContext());
                                tv6.setText(String.valueOf(res.getInt(7)));
                                tv6.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv7 = new TextView(customView.getContext());
                                tv7.setText(String.valueOf(res.getInt(8)));
                                tv7.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv8 = new TextView(customView.getContext());
                                tv8.setText(String.valueOf(res.getInt(9)));
                                tv8.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv9 = new TextView(customView.getContext());
                                tv9.setText(String.valueOf(res.getInt(10)));
                                tv9.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv10 = new TextView(customView.getContext());
                                tv10.setText(String.valueOf(res.getInt(11)));
                                tv10.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv11 = new TextView(customView.getContext());
                                tv11.setText(String.valueOf(res.getInt(12)));
                                tv11.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvjSoda = new TextView(customView.getContext());
                                tvjSoda.setText(String.valueOf(res.getInt(13)));
                                tvjSoda.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvSsoda = new TextView(customView.getContext());
                                tvSsoda.setText(String.valueOf(res.getInt(14)));
                                tvSsoda.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvWater = new TextView(customView.getContext());
                                tvWater.setText(String.valueOf(res.getInt(15)));
                                tvWater.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvLassi = new TextView(customView.getContext());
                                tvLassi.setText(String.valueOf(res.getInt(16)));
                                tvLassi.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv12 = new TextView(customView.getContext());
                                tv12.setText(String.valueOf(res.getInt(17)));
                                tv12.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv13 = new TextView(customView.getContext());
                                tv13.setText(String.valueOf(res.getInt(18)));
                                tv13.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv14 = new TextView(customView.getContext());
                                tv14.setText(String.valueOf(res.getInt(19)));
                                tv14.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv15 = new TextView(customView.getContext());
                                tv15.setText(String.valueOf(res.getInt(20)));
                                tv15.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv16 = new TextView(customView.getContext());
                                tv16.setText(String.valueOf(res.getInt(21)));
                                tv16.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvKulfi = new TextView(customView.getContext());
                                tvKulfi.setText(String.valueOf(res.getInt(22)));
                                tvKulfi.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvCbar = new TextView(customView.getContext());
                                tvCbar.setText(String.valueOf(res.getInt(23)));
                                tvCbar.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvFpack = new TextView(customView.getContext());
                                tvFpack.setText(String.valueOf(res.getInt(24)));
                                tvFpack.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvOther = new TextView(customView.getContext());
                                tvOther.setText(String.valueOf(res.getInt(25)));
                                tvOther.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvOther1 = new TextView(customView.getContext());
                                tvOther1.setText(String.valueOf(res.getInt(26)));
                                tvOther1.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv17 = new TextView(customView.getContext());
                                tv17.setText(String.valueOf(res.getInt(27)));
                                tv17.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv18 = new TextView(customView.getContext());
                                tv18.setText(String.valueOf(res.getString(28)));
                                tv18.setGravity(Gravity.CENTER_HORIZONTAL);

                                if (isDarkThemeOn) {
                                    cname.setTextColor(Color.WHITE);
                                    tb0.setTextColor(Color.WHITE);
                                    tb1.setTextColor(Color.WHITE);
                                    tb2.setTextColor(Color.WHITE);
                                    tb3.setTextColor(Color.WHITE);
                                    tb4.setTextColor(Color.WHITE);
                                    tb5.setTextColor(Color.WHITE);
                                    tb6.setTextColor(Color.WHITE);
                                    tb7.setTextColor(Color.WHITE);
                                    tb8.setTextColor(Color.WHITE);
                                    tb9.setTextColor(Color.WHITE);
                                    tb10.setTextColor(Color.WHITE);
                                    tb11.setTextColor(Color.WHITE);
                                    tb12.setTextColor(Color.WHITE);
                                    tb13.setTextColor(Color.WHITE);
                                    tb14.setTextColor(Color.WHITE);
                                    tb15.setTextColor(Color.WHITE);
                                    tb16.setTextColor(Color.WHITE);
                                    tb17.setTextColor(Color.WHITE);
                                    tbJsoda.setTextColor(Color.WHITE);
                                    tbSsoda.setTextColor(Color.WHITE);
                                    tbKulfi.setTextColor(Color.WHITE);
                                    tbCbar.setTextColor(Color.WHITE);
                                    tbFpack.setTextColor(Color.WHITE);
                                    tbWater.setTextColor(Color.WHITE);
                                    tbLassi.setTextColor(Color.WHITE);
                                    tbOther.setTextColor(Color.WHITE);
                                    tbOther1.setTextColor(Color.WHITE);
                                } else {
                                    cname.setTextColor(Color.BLACK);
                                    tb0.setTextColor(Color.BLACK);
                                    tb1.setTextColor(Color.BLACK);
                                    tb2.setTextColor(Color.BLACK);
                                    tb3.setTextColor(Color.BLACK);
                                    tb4.setTextColor(Color.BLACK);
                                    tb5.setTextColor(Color.BLACK);
                                    tb6.setTextColor(Color.BLACK);
                                    tb7.setTextColor(Color.BLACK);
                                    tb8.setTextColor(Color.BLACK);
                                    tb9.setTextColor(Color.BLACK);
                                    tb10.setTextColor(Color.BLACK);
                                    tb11.setTextColor(Color.BLACK);
                                    tb12.setTextColor(Color.BLACK);
                                    tb13.setTextColor(Color.BLACK);
                                    tb14.setTextColor(Color.BLACK);
                                    tb15.setTextColor(Color.BLACK);
                                    tb16.setTextColor(Color.BLACK);
                                    tb17.setTextColor(Color.BLACK);
                                    tbJsoda.setTextColor(Color.BLACK);
                                    tbSsoda.setTextColor(Color.BLACK);
                                    tbKulfi.setTextColor(Color.BLACK);
                                    tbCbar.setTextColor(Color.BLACK);
                                    tbFpack.setTextColor(Color.BLACK);
                                    tbWater.setTextColor(Color.BLACK);
                                    tbLassi.setTextColor(Color.BLACK);
                                    tbOther.setTextColor(Color.BLACK);
                                    tbOther1.setTextColor(Color.BLACK);
                                }
                                tableRow.addView(tv1);
                                tableRow.addView(cname1);
                                tableRow.addView(tv2);
                                tableRow.addView(tv3);
                                tableRow.addView(tv4);
                                tableRow.addView(tv5);
                                tableRow.addView(tv6);
                                tableRow.addView(tv7);
                                tableRow.addView(tv8);
                                tableRow.addView(tv9);
                                tableRow.addView(tv10);
                                tableRow.addView(tv11);
                                tableRow.addView(tvjSoda);
                                tableRow.addView(tvSsoda);
                                tableRow.addView(tvWater);
                                tableRow.addView(tvLassi);
                                tableRow.addView(tv12);
                                tableRow.addView(tv13);
                                tableRow.addView(tv14);
                                tableRow.addView(tv15);
                                tableRow.addView(tv16);
                                tableRow.addView(tvKulfi);
                                tableRow.addView(tvCbar);
                                tableRow.addView(tvFpack);
                                tableRow.addView(tvOther);
                                tableRow.addView(tvOther1);
                                tableRow.addView(tv17);
                                tableRow.addView(tv18);
//                    }
                                tableLayout.addView(tableRow);

                            }
                            builder.setMessage(sb);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(getActivity(), "No record found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Enter valid Name", Toast.LENGTH_SHORT).show();
                    }
                } else if (spinner.getSelectedItem().equals("Bill Wise")) {
                    billName.requestFocus();
                    if (billName.getText().length() > 0) {
                        res = db.getOne(Integer.parseInt(String.valueOf(billName.getText())));
                        if (res.getCount() > 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            StringBuffer sb = new StringBuffer();
                            builder.setTitle("Record Found");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            Cursor finalRes = res;
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    doGeneratePdf(finalRes);
                                }
                            });
                            while (res.moveToNext()) {
//                                sb.append(res.getInt(1)+"  "+res.getInt(18)+"  "+res.getString(19)+"\n");
                                sb.append("Total: " + res.getInt(27));
                                TableRow tableRow = new TableRow(customView.getContext());
                                tableRow.setPadding(10, 10, 10, 10);
                                TextView tv1 = new TextView(customView.getContext());
                                tv1.setText(String.valueOf(res.getInt(1)));
                                tv1.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView cname1 = new TextView(customView.getContext());
                                cname1.setText(String.valueOf(res.getString(2)));
                                cname1.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv2 = new TextView(customView.getContext());
                                tv2.setText(String.valueOf(res.getInt(3)));
                                tv2.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv3 = new TextView(customView.getContext());
                                tv3.setText(String.valueOf(res.getInt(4)));
                                tv3.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv4 = new TextView(customView.getContext());
                                tv4.setText(String.valueOf(res.getInt(5)));
                                tv4.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv5 = new TextView(customView.getContext());
                                tv5.setText(String.valueOf(res.getInt(6)));
                                tv5.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv6 = new TextView(customView.getContext());
                                tv6.setText(String.valueOf(res.getInt(7)));
                                tv6.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv7 = new TextView(customView.getContext());
                                tv7.setText(String.valueOf(res.getInt(8)));
                                tv7.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv8 = new TextView(customView.getContext());
                                tv8.setText(String.valueOf(res.getInt(9)));
                                tv8.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv9 = new TextView(customView.getContext());
                                tv9.setText(String.valueOf(res.getInt(10)));
                                tv9.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv10 = new TextView(customView.getContext());
                                tv10.setText(String.valueOf(res.getInt(11)));
                                tv10.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv11 = new TextView(customView.getContext());
                                tv11.setText(String.valueOf(res.getInt(12)));
                                tv11.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvJsoda = new TextView(customView.getContext());
                                tvJsoda.setText(String.valueOf(res.getInt(13)));
                                tvJsoda.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvSsoda = new TextView(customView.getContext());
                                tvSsoda.setText(String.valueOf(res.getInt(14)));
                                tvSsoda.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvWater = new TextView(customView.getContext());
                                tvWater.setText(String.valueOf(res.getInt(15)));
                                tvWater.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvLassi = new TextView(customView.getContext());
                                tvLassi.setText(String.valueOf(res.getInt(16)));
                                tvLassi.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv12 = new TextView(customView.getContext());
                                tv12.setText(String.valueOf(res.getInt(17)));
                                tv12.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv13 = new TextView(customView.getContext());
                                tv13.setText(String.valueOf(res.getInt(18)));
                                tv13.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv14 = new TextView(customView.getContext());
                                tv14.setText(String.valueOf(res.getInt(19)));
                                tv14.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv15 = new TextView(customView.getContext());
                                tv15.setText(String.valueOf(res.getInt(20)));
                                tv15.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv16 = new TextView(customView.getContext());
                                tv16.setText(String.valueOf(res.getInt(21)));
                                tv16.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvKulfi = new TextView(customView.getContext());
                                tvKulfi.setText(String.valueOf(res.getInt(22)));
                                tvKulfi.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvCbar = new TextView(customView.getContext());
                                tvCbar.setText(String.valueOf(res.getInt(23)));
                                tvCbar.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvFpack = new TextView(customView.getContext());
                                tvFpack.setText(String.valueOf(res.getInt(24)));
                                tvFpack.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvOther = new TextView(customView.getContext());
                                tvOther.setText(String.valueOf(res.getInt(25)));
                                tvOther.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tvOther1 = new TextView(customView.getContext());
                                tvOther1.setText(String.valueOf(res.getInt(26)));
                                tvOther1.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv17 = new TextView(customView.getContext());
                                tv17.setText(String.valueOf(res.getInt(27)));
                                tv17.setGravity(Gravity.CENTER_HORIZONTAL);

                                TextView tv18 = new TextView(customView.getContext());
                                tv18.setText(String.valueOf(res.getString(28)));
                                tv18.setGravity(Gravity.CENTER_HORIZONTAL);

                                Button btnPrint = new Button(customView.getContext());
                                btnPrint.setText(String.valueOf("Print"));
                                btnPrint.setGravity(Gravity.CENTER_HORIZONTAL);

                                Cursor finalRes1 = res;
                                btnPrint.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editor.putBoolean("isReprint",true).apply();
                                        Map<String,Item> map = new HashMap<>();
                                        int orange = prefs.getInt("orange", 15);
                                        int kokam = prefs.getInt("kokam", 20);
                                        int lemon = prefs.getInt("lemon", 15);
                                        int sarbat = prefs.getInt("sarbat", 15);
                                        int pachak = prefs.getInt("pachak", 20);
                                        int wala = prefs.getInt("wala", 20);
                                        int lSoda = prefs.getInt("lsoda", 15);
                                        int ssrbt = prefs.getInt("ssrbt", 20);
                                        int lorange = prefs.getInt("lorange", 20);
                                        int Llemon = prefs.getInt("llemon", 20);
                                        int jeera = prefs.getInt("jsoda",15);
                                        int sSoda = prefs.getInt("ssoda",15);
                                        int water = prefs.getInt("water",10);
                                        int lassi = prefs.getInt("lassi",20);

                                        int vanilla = prefs.getInt("vanilla",10);
                                        int mango = prefs.getInt("mango",20);
                                        int cbar = prefs.getInt("cbar",10);
                                        int sbry = prefs.getInt("sbry",10);
                                        int btrSch = prefs.getInt("btrsch",20);
                                        int kulfi = prefs.getInt("kulfi",10);
                                        int pista = prefs.getInt("pista",15);
                                        int fpack = prefs.getInt("fpack",100);
//                                        if(!tv1.getText().equals("0")){
//                                            map.put(finalRes1.getColumnName(1),cname1.getText());
//                                        }else{
//                                            map.remove(finalRes1.getColumnName(1));
//                                        }

//                                        if(!cname1.getText().equals("")){
//                                            map.put(finalRes1.getColumnName(2),cname1.getText());
//                                        }else{
//                                            map.remove(finalRes1.getColumnName(2));
//                                        }

                                        //

                                        if(!tv2.getText().equals("0")){
                                            int orangeTotal = Integer.parseInt(String.valueOf(tv2.getText()));
                                            Item orangeObj = new Item(orange, orangeTotal / orange, orangeTotal);
                                            map.put(finalRes1.getColumnName(3),orangeObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(3));
                                        }

                                        if(!tv3.getText().equals("0")){
                                            int kokamTotal = Integer.parseInt(String.valueOf(tv3.getText()));
                                            Item kokamObj = new Item(kokam, kokamTotal / kokam, kokamTotal);
                                            map.put(finalRes1.getColumnName(4),kokamObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(4));
                                        }

                                        if(!tv4.getText().equals("0")){
                                            int lemonTotal = Integer.parseInt(String.valueOf(tv4.getText()));
                                            Item lemonObj = new Item(lemon, lemonTotal / lemon, lemonTotal);
                                            map.put(finalRes1.getColumnName(5),lemonObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(5));
                                        }

                                        if(!tv5.getText().equals("0")){
                                            int sarbatTotal = Integer.parseInt(String.valueOf(tv5.getText()));
                                            Item sarbatObj = new Item(sarbat, sarbatTotal / sarbat, sarbatTotal);
                                            map.put(finalRes1.getColumnName(6),sarbatObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(6));
                                        }

                                        if(!tv6.getText().equals("0")){
                                            int pachakTotal = Integer.parseInt(String.valueOf(tv6.getText()));
                                            Item pachakObj = new Item(pachak, pachakTotal / pachak, pachakTotal);
                                            map.put(finalRes1.getColumnName(7),pachakObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(7));
                                        }

                                        if(!tv7.getText().equals("0")){
                                            int walaTotal = Integer.parseInt(String.valueOf(tv7.getText()));
                                            Item walaObj = new Item(wala, walaTotal / wala, walaTotal);
                                            map.put(finalRes1.getColumnName(8),walaObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(8));
                                        }

                                        if(!tv8.getText().equals("0")){
                                            int lSodaTotal = Integer.parseInt(String.valueOf(tv8.getText()));
                                            Item lsodaObj = new Item(lSoda, lSodaTotal / lSoda, lSodaTotal);
                                            map.put(finalRes1.getColumnName(9),lsodaObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(9));
                                        }

                                        if(!tv9.getText().equals("0")){
                                            int ssrbtTotal = Integer.parseInt(String.valueOf(tv9.getText()));
                                            Item ssrbtObj = new Item(ssrbt, ssrbtTotal / ssrbt, ssrbtTotal);
                                            map.put(finalRes1.getColumnName(10),ssrbtObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(10));
                                        }

                                        if(!tv10.getText().equals("0")){
                                            int lorangeTotal = Integer.parseInt(String.valueOf(tv10.getText()));
                                            Item lorangeObj = new Item(lorange, lorangeTotal / lorange, lorangeTotal);
                                            map.put(finalRes1.getColumnName(11),lorangeObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(11));
                                        }

                                        if(!tv11.getText().equals("0")){
                                            int LlemonTotal = Integer.parseInt(String.valueOf(tv11.getText()));
                                            Item llemonObj = new Item(Llemon, LlemonTotal / Llemon, LlemonTotal);
                                            map.put(finalRes1.getColumnName(12),llemonObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(12));
                                        }

                                        if(!tvJsoda.getText().equals("0")){
                                            int jeeraTotal = Integer.parseInt(String.valueOf(tvJsoda.getText()));
                                            Item jsodaObj = new Item(jeera, jeeraTotal / jeera, jeeraTotal);
                                            map.put(finalRes1.getColumnName(13),jsodaObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(13));
                                        }

                                        if(!tvSsoda.getText().equals("0")){
                                            int sSodaTotal = Integer.parseInt(String.valueOf(tvSsoda.getText()));
                                            Item sSodaObj = new Item(sSoda, sSodaTotal / sSoda, sSodaTotal);
                                            map.put(finalRes1.getColumnName(14),sSodaObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(14));
                                        }

                                        if(!tvWater.getText().equals("0")){
                                            int waterTotal = Integer.parseInt(String.valueOf(tvWater.getText()));
                                            Item waterObj = new Item(water, waterTotal / water, waterTotal);
                                            map.put(finalRes1.getColumnName(15),waterObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(15));
                                        }

                                        if(!tvLassi.getText().equals("0")){
                                            int lassiTotal = Integer.parseInt(String.valueOf(tvLassi.getText()));
                                            Item lassiObj = new Item(lassi, lassiTotal / lassi, lassiTotal);
                                            map.put(finalRes1.getColumnName(16),lassiObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(16));
                                        }

                                        //
                                        if(!tv12.getText().equals("0")){
                                            int vanillaTotal= Integer.parseInt(String.valueOf(tv12.getText()));
                                            Item vanillaObj = new Item(vanilla, vanillaTotal / vanilla, vanillaTotal);
                                            map.put(finalRes1.getColumnName(17),vanillaObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(17));
                                        }

                                        if(!tv13.getText().equals("0")){
                                            int pistaTotal =Integer.parseInt(String.valueOf(tv13.getText()));
                                            Item pistaObj = new Item(pista, pistaTotal / pista, pistaTotal);
                                            map.put(finalRes1.getColumnName(18),pistaObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(18));
                                        }

                                        if(!tv14.getText().equals("0")){
                                            int sbryTotal =Integer.parseInt(String.valueOf(tv14.getText()));
                                            Item sbryObj = new Item(sbry, sbryTotal / sbry, sbryTotal);
                                            map.put(finalRes1.getColumnName(19),sbryObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(19));
                                        }

                                        if(!tv15.getText().equals("0")){
                                            int mangoTotal = Integer.parseInt(String.valueOf(tv15.getText()));
                                            Item mangoObj = new Item(mango, mangoTotal / mango, mangoTotal);
                                            map.put(finalRes1.getColumnName(20),mangoObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(20));
                                        }

                                        if(!tv16.getText().equals("0")){
                                            int btrSchTotal = Integer.parseInt(String.valueOf(tv16.getText()));
                                            Item btrschObj = new Item(btrSch, btrSchTotal / btrSch, btrSchTotal);
                                            map.put(finalRes1.getColumnName(21),btrschObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(21));
                                        }

                                        if(!tvKulfi.getText().equals("0")){
                                            int kulfiTotal = Integer.parseInt(String.valueOf(tvKulfi.getText()));
                                            Item kulfiObj = new Item(kulfi, kulfiTotal / kulfi, kulfiTotal);
                                            map.put(finalRes1.getColumnName(22),kulfiObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(22));
                                        }

                                        if(!tvCbar.getText().equals("0")){
                                            int cbarTotal = Integer.parseInt(String.valueOf(tvCbar.getText()));
                                            Item cBarObj = new Item(cbar, cbarTotal / cbar, cbarTotal);
                                            map.put(finalRes1.getColumnName(23),cBarObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(23));
                                        }

                                        if(!tvFpack.getText().equals("0")){
                                            int fpackTotal =  Integer.parseInt(String.valueOf(tvFpack.getText()));
                                            Item fpackObj = new Item(fpack, fpackTotal / fpack, fpackTotal);
                                            map.put(finalRes1.getColumnName(24),fpackObj);
                                        }else{
                                            map.remove(finalRes1.getColumnName(24));
                                        }

                                        if(!tvOther.getText().equals("0")){
                                            map.put(finalRes1.getColumnName(25),new Item(0,0,Integer.parseInt(String.valueOf(tvOther.getText()))));
                                        }else{
                                            map.remove(finalRes1.getColumnName(25));
                                        }

                                        if(!tvOther1.getText().equals("0")){
                                            map.put(finalRes1.getColumnName(26),new Item(0,0,Integer.parseInt(String.valueOf(tvOther1.getText()))));
                                        }else{
                                            map.remove(finalRes1.getColumnName(26));
                                        }

//                                        if(!tv17.getText().equals("0")){
//                                            map.put(finalRes1.getColumnName(27),tv17.getText());
//                                        }else{
//                                            map.remove(finalRes1.getColumnName(27));
//                                        }
//
//                                        if(!tv18.getText().equals("0")){
//                                            map.put(finalRes1.getColumnName(28),tv18.getText());
//                                        }else{
//                                            map.remove(finalRes1.getColumnName(28));
//                                        }

                                        System.out.println(map.entrySet());
                                        Long billNou = Long.parseLong(String.valueOf(tv1.getText()));
                                        int finalCostu = Integer.parseInt(String.valueOf(tv17.getText()));

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                            ((MainActivity)getActivity()).printInvoice(billNou,map,finalCostu,String.valueOf(tv18.getText()),String.valueOf(cname1.getText()));
                                        }
                                        editor.putBoolean("isReprint",false).apply();
                                    }
                                });

                                if (isDarkThemeOn) {
                                    cname.setTextColor(Color.WHITE);
                                    tb0.setTextColor(Color.WHITE);
                                    tb1.setTextColor(Color.WHITE);
                                    tb2.setTextColor(Color.WHITE);
                                    tb3.setTextColor(Color.WHITE);
                                    tb4.setTextColor(Color.WHITE);
                                    tb5.setTextColor(Color.WHITE);
                                    tb6.setTextColor(Color.WHITE);
                                    tb7.setTextColor(Color.WHITE);
                                    tb8.setTextColor(Color.WHITE);
                                    tb9.setTextColor(Color.WHITE);
                                    tb10.setTextColor(Color.WHITE);
                                    tb11.setTextColor(Color.WHITE);
                                    tb12.setTextColor(Color.WHITE);
                                    tb13.setTextColor(Color.WHITE);
                                    tb14.setTextColor(Color.WHITE);
                                    tb15.setTextColor(Color.WHITE);
                                    tb16.setTextColor(Color.WHITE);
                                    tb17.setTextColor(Color.WHITE);
                                    tbJsoda.setTextColor(Color.WHITE);
                                    tbSsoda.setTextColor(Color.WHITE);
                                    tbKulfi.setTextColor(Color.WHITE);
                                    tbCbar.setTextColor(Color.WHITE);
                                    tbFpack.setTextColor(Color.WHITE);
                                    tbWater.setTextColor(Color.WHITE);
                                    tbLassi.setTextColor(Color.WHITE);
                                    tbOther.setTextColor(Color.WHITE);
                                    tbOther1.setTextColor(Color.WHITE);
                                    btnPrint.setTextColor(Color.WHITE);
                                } else {
                                    cname.setTextColor(Color.BLACK);
                                    tb0.setTextColor(Color.BLACK);
                                    tb1.setTextColor(Color.BLACK);
                                    tb2.setTextColor(Color.BLACK);
                                    tb3.setTextColor(Color.BLACK);
                                    tb4.setTextColor(Color.BLACK);
                                    tb5.setTextColor(Color.BLACK);
                                    tb6.setTextColor(Color.BLACK);
                                    tb7.setTextColor(Color.BLACK);
                                    tb8.setTextColor(Color.BLACK);
                                    tb9.setTextColor(Color.BLACK);
                                    tb10.setTextColor(Color.BLACK);
                                    tb11.setTextColor(Color.BLACK);
                                    tb12.setTextColor(Color.BLACK);
                                    tb13.setTextColor(Color.BLACK);
                                    tb14.setTextColor(Color.BLACK);
                                    tb15.setTextColor(Color.BLACK);
                                    tb16.setTextColor(Color.BLACK);
                                    tb17.setTextColor(Color.BLACK);
                                    tbJsoda.setTextColor(Color.BLACK);
                                    tbSsoda.setTextColor(Color.BLACK);
                                    tbKulfi.setTextColor(Color.BLACK);
                                    tbCbar.setTextColor(Color.BLACK);
                                    tbFpack.setTextColor(Color.BLACK);
                                    tbWater.setTextColor(Color.BLACK);
                                    tbLassi.setTextColor(Color.BLACK);
                                    tbOther.setTextColor(Color.BLACK);
                                    tbOther1.setTextColor(Color.BLACK);
                                    btnPrint.setTextColor(Color.BLACK);
                                }
                                tableRow.addView(tv1);
                                tableRow.addView(cname1);
                                tableRow.addView(tv2);
                                tableRow.addView(tv3);
                                tableRow.addView(tv4);
                                tableRow.addView(tv5);
                                tableRow.addView(tv6);
                                tableRow.addView(tv7);
                                tableRow.addView(tv8);
                                tableRow.addView(tv9);
                                tableRow.addView(tv10);
                                tableRow.addView(tv11);
                                tableRow.addView(tvJsoda);
                                tableRow.addView(tvSsoda);
                                tableRow.addView(tvWater);
                                tableRow.addView(tvLassi);
                                tableRow.addView(tv12);
                                tableRow.addView(tv13);
                                tableRow.addView(tv14);
                                tableRow.addView(tv15);
                                tableRow.addView(tv16);
                                tableRow.addView(tvKulfi);
                                tableRow.addView(tvCbar);
                                tableRow.addView(tvFpack);
                                tableRow.addView(tvOther);
                                tableRow.addView(tvOther1);
                                tableRow.addView(tv17);
                                tableRow.addView(tv18);
                                tableRow.addView(btnPrint);
//                    }
                                tableLayout.addView(tableRow);
                            }
                            builder.setMessage(sb);
                            builder.setView(customView);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Toast.makeText(getContext(), "No record found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Enter valid billno", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Enter valid details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: {
                        billName.setEnabled(false);
                        fromDate.setEnabled(true);
                        toDate.setEnabled(true);
                        billName.setText(null);
                    }
                    break;
                    case 1:
                    case 2: {
                        if (position == 1) {
                            billName.setHint("Enter name");
                            billName.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                        } else if (position == 2) {
                            billName.setHint("Enter Billno");
                            billName.setInputType(InputType.TYPE_CLASS_NUMBER);
                        }
                        fromDate.setEnabled(false);
                        fromDate.setText("From date");
                        toDate.setEnabled(false);
                        toDate.setText("To date");
                        billName.setEnabled(true);
                        billName.setText(null);
                    }
                    break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                fromDate.setEnabled(false);
                toDate.setEnabled(false);
                billName.setEnabled(false);
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String day1, month1;
                        if (view.getDayOfMonth() < 10) {
                            day1 = "0" + view.getDayOfMonth();
                        } else {
                            day1 = "" + view.getDayOfMonth();
                        }
                        if ((view.getMonth() + 1) < 10) {
                            month1 = "0" + (view.getMonth() + 1);
                        } else {
                            month1 = "" + (view.getMonth() + 1);
                        }
                        String min = day1 + "-" + month1 + "-" + view.getYear();
                        fromDate.setText(day1 + "/" + month1 + "/" + view.getYear());
                        fromDateFmt = view.getYear()+"/"+ month1 + "/" + day1;
                        System.out.println(fromDateFmt);
                        try {
                            DateFormat formatter;
                            Date date;
                            formatter = new SimpleDateFormat("dd-MM-yyyy");
                            date = (Date) formatter.parse(min);
                            longDate = date.getTime();
                            System.out.println("<<<<<<<<" + longDate);
                            view.setMaxDate(longDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                if (toPutMaxDate != 0) {
                    datePickerDialog.getDatePicker().setMaxDate(toPutMaxDate);
                } else {
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
                datePickerDialog.show();
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        String day1, month1;
                        if (view.getDayOfMonth() < 10) {
                            day1 = "0" + view.getDayOfMonth();
                        } else {
                            day1 = "" + view.getDayOfMonth();
                        }
                        if ((view.getMonth() + 1) < 10) {
                            month1 = "0" + (view.getMonth() + 1);
                        } else {
                            month1 = "" + (view.getMonth() + 1);
                        }
                        String min = day1 + "-" + month1 + "-" + view.getYear();
                        toDate.setText(day1 + "/" + month1 + "/" + view.getYear());
                        toDateFmt = view.getYear() +"/"+ month1 +"/"+ day1;
                        System.out.println(toDateFmt);
                        try {
                            DateFormat formatter;
                            Date date;
                            formatter = new SimpleDateFormat("dd-MM-yyyy");
                            date = (Date) formatter.parse(min);
                            toPutMaxDate = date.getTime();
                            System.out.println("<<<<<<<<" + toPutMaxDate);
                            view.setMaxDate(toPutMaxDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(longDate);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        return view;
    }

    private void fillTable(View v) {
        boolean isDarkThemeOn = (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        DbManager db = new DbManager(getContext());
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        int todayTotal = 0;
        int totalAll = 0;

        Date d = calendar.getTime();
        Cursor res = db.findRange(sdf.format(d.getTime()),sdf.format(d.getTime()));
        int srno = 0;
        String currDate = "";
        if(res.getCount()>0){
//            TableRow row1 = new TableRow(getContext());
//            row1.setPadding(55,0,25,0);
//            TextView h1 = new TextView(getContext());
//            h1.setTextColor(Color.BLACK);
//            h1.setText(String.valueOf("No"));
//            TextView h2 = new TextView(getContext());
//            h2.setText(String.valueOf("Total"));
//            h2.setTextColor(Color.BLACK);
//            TextView h3 = new TextView(getContext());
//            h3.setText(String.valueOf("Date"));
//            h3.setTextColor(Color.BLACK);
//            row1.addView(h1);
//            row1.addView(h2);
//            row1.addView(h3);
//            t.addView(row1);
            if(isDarkThemeOn){
//                srno1 = v.findViewById(R.id.serialNo);
//                tvTotal = v.findViewById(R.id.total1);
//                currDate1 = v.findViewById(R.id.currDate);
                srno1.setTextColor(Color.WHITE);
                tvTotal.setTextColor(Color.WHITE);
                currDate1.setTextColor(Color.WHITE);
            }else{
                srno1.setTextColor(Color.BLACK);
                tvTotal.setTextColor(Color.BLACK);
                currDate1.setTextColor(Color.BLACK);
            }
            while(res.moveToNext()){
                TableRow row = new TableRow(getContext());
                row.setPadding(50,10,25,10);
                todayTotal = res.getInt(27);
                srno = res.getInt(1);
                currDate = res.getString(28);

                srno1 = new TextView(getContext());
                srno1.setText(String.valueOf(srno));
                srno1.setPadding(80,0,25,0);

                tvTotal = new TextView(getContext());
                tvTotal.setText(String.valueOf(todayTotal));
                Bundle b = new Bundle();

                if(!String.valueOf(res.getInt(3)).equals("0")){
                    b.putInt("orange", Integer.parseInt(String.valueOf(res.getInt(3))));
                }
                if(!String.valueOf(res.getInt(4)).equals("0")){
                    b.putInt("kokam", Integer.parseInt(String.valueOf(res.getInt(4))));
                }
                if(!String.valueOf(res.getInt(5)).equals("0")){
                    b.putInt("lemon", Integer.parseInt(String.valueOf(res.getInt(5))));
                }
                if(!String.valueOf(res.getInt(6)).equals("0")) {
                    b.putInt("sarbat", Integer.parseInt(String.valueOf(res.getInt(6))));
                }
                if(!String.valueOf(res.getInt(7)).equals("0")) {
                    b.putInt("pachak", Integer.parseInt(String.valueOf(res.getInt(7))));
                }
                if(!String.valueOf(res.getInt(8)).equals("0")) {
                    b.putInt("wala", Integer.parseInt(String.valueOf(res.getInt(8))));
                }
                if(!String.valueOf(res.getInt(9)).equals("0")) {
                    b.putInt("lsoda", Integer.parseInt(String.valueOf(res.getInt(9))));
                }
                if(!String.valueOf(res.getInt(10)).equals("0")) {
                    b.putInt("ssrbt", Integer.parseInt(String.valueOf(res.getInt(10))));
                }
                if(!String.valueOf(res.getInt(11)).equals("0")) {
                    b.putInt("lorange", Integer.parseInt(String.valueOf(res.getInt(11))));
                }
                if(!String.valueOf(res.getInt(12)).equals("0")) {
                    b.putInt("llemon", Integer.parseInt(String.valueOf(res.getInt(12))));
                }
                if(!String.valueOf(res.getInt(13)).equals("0")) {
                    b.putInt("jsoda", Integer.parseInt(String.valueOf(res.getInt(13))));
                }
                if(!String.valueOf(res.getInt(14)).equals("0")) {
                    b.putInt("sSoda", Integer.parseInt(String.valueOf(res.getInt(14))));
                }
                if(!String.valueOf(res.getInt(15)).equals("0")) {
                    b.putInt("water", Integer.parseInt(String.valueOf(res.getInt(15))));
                }
                if(!String.valueOf(res.getInt(16)).equals("0")) {
                    b.putInt("lassi", Integer.parseInt(String.valueOf(res.getInt(16))));
                }
                if(!String.valueOf(res.getInt(17)).equals("0")) {
                    b.putInt("vanilla", Integer.parseInt(String.valueOf(res.getInt(17))));
                }
                if(!String.valueOf(res.getInt(18)).equals("0")) {
                    b.putInt("pista", Integer.parseInt(String.valueOf(res.getInt(18))));
                }
                if(!String.valueOf(res.getInt(19)).equals("0")) {
                    b.putInt("stwbry", Integer.parseInt(String.valueOf(res.getInt(19))));
                }
                if(!String.valueOf(res.getInt(20)).equals("0")) {
                    b.putInt("mango", Integer.parseInt(String.valueOf(res.getInt(20))));
                }
                if(!String.valueOf(res.getInt(21)).equals("0")) {
                    b.putInt("btrsch", Integer.parseInt(String.valueOf(res.getInt(21))));
                }
                if(!String.valueOf(res.getInt(22)).equals("0")) {
                    b.putInt("kulfi", Integer.parseInt(String.valueOf(res.getInt(22))));
                }
                if(!String.valueOf(res.getInt(23)).equals("0")) {
                    b.putInt("cbar", Integer.parseInt(String.valueOf(res.getInt(23))));
                }
                if(!String.valueOf(res.getInt(24)).equals("0")) {
                    b.putInt("fpack", Integer.parseInt(String.valueOf(res.getInt(24))));
                }
                if(!String.valueOf(res.getInt(25)).equals("0")) {
                    b.putInt("other", Integer.parseInt(String.valueOf(res.getInt(25))));
                }
                if(!String.valueOf(res.getInt(26)).equals("0")) {
                    b.putInt("other1", Integer.parseInt(String.valueOf(res.getInt(26))));
                }
//                if(!String.valueOf(res.getInt(28)).equals("0")) {
//                    b.putString("datetime", (String.valueOf(res.getString(28))));
//                }

                Bundle bundle = new Bundle();
                bundle.putAll(b);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    StringBuilder str = new StringBuilder();
                    if(bundle.getInt("orange") != 0){
                        str.append("orange:"+bundle.getInt("orange"));
                    }
                    if(bundle.getInt("kokam") != 0){
                        str.append(" kokam:"+bundle.getInt("kokam"));
                    }
                    if(bundle.getInt("lemon") != 0){
                        str.append(" lemon:"+bundle.getInt("lemon"));
                    }
                    if(bundle.getInt("sarbat") != 0){
                        str.append(" sarbat:"+bundle.getInt("sarbat"));
                    }
                    if(bundle.getInt("pachak") != 0){
                        str.append(" pachak:"+bundle.getInt("pachak"));
                    }
                    if(bundle.getInt("wala") != 0){
                        str.append(" wala:"+bundle.getInt("wala"));
                    }
                    if(bundle.getInt("lsoda") != 0){
                        str.append(" lsoda:"+bundle.getInt("lsoda"));
                    }
                    if(bundle.getInt("ssrbt") != 0){
                        str.append(" ssrbt:"+bundle.getInt("ssrbt"));
                    }
                    if(bundle.getInt("lorange") != 0){
                        str.append(" lorange:"+bundle.getInt("lorange"));
                    }
                    if(bundle.getInt("llemon") != 0){
                        str.append(" llemon:"+bundle.getInt("llemon"));
                    }
                    if(bundle.getInt("jsoda") != 0){
                        str.append(" jsoda:"+bundle.getInt("jsoda"));
                    }
                    if(bundle.getInt("sSoda") != 0){
                        str.append(" sSoda:"+bundle.getInt("sSoda"));
                    }
                    if(bundle.getInt("water") != 0){
                        str.append(" water:"+bundle.getInt("water"));
                    }
                    if(bundle.getInt("lassi") != 0){
                        str.append(" lassi:"+bundle.getInt("lassi"));
                    }
                    if(bundle.getInt("vanilla") != 0){
                        str.append(" vanilla:"+bundle.getInt("vanilla"));
                    }
                    if(bundle.getInt("pista") != 0){
                        str.append(" pista:"+bundle.getInt("pista"));
                    }
                    if(bundle.getInt("stwbry") != 0){
                        str.append(" stwbry:"+bundle.getInt("stwbry"));
                    }
                    if(bundle.getInt("mango") != 0){
                        str.append(" mango:"+bundle.getInt("mango"));
                    }
                    if(bundle.getInt("btrsch") != 0){
                        str.append(" btrsch:"+bundle.getInt("btrsch"));
                    }
                    if(bundle.getInt("kulfi") != 0){
                        str.append(" kulfi:"+bundle.getInt("kulfi"));
                    }
                    if(bundle.getInt("cbar") != 0){
                        str.append(" cbar:"+bundle.getInt("cbar"));
                    }
                    if(bundle.getInt("fpack") != 0){
                        str.append(" fpack:"+bundle.getInt("fpack"));
                    }
                    if(bundle.getInt("other") != 0){
                        str.append(" other:"+bundle.getInt("other"));
                    }
                    if(bundle.getInt("other1") != 0){
                        str.append(" other1:"+bundle.getInt("other1"));
                    }
//                    if(bundle.getString("datetime") != ""){
//                        str.append(" datetime:"+bundle.getString("datetime"));
//                    }
//                    tvTotal.setTooltipText(String.valueOf("Orange:"+bundle.get("orange")+", Kokam:"+bundle.get("kokam")+", Lemon:"+bundle.get("lemon")+", Sarbat:"+bundle.get("sarbat")+", Pachak:"+bundle.getInt("pachak")+", Wala"+bundle.getInt("wala")+", Lsoda:"+bundle.getInt("lsoda")+", ssrbt:"+bundle.getInt("ssrbt")+", Lorange:"+bundle.getInt("lorange")+", Llemon:"+bundle.getInt("llemon")+", jSoda:"+bundle.getInt("jsoda")+", sSoda:"+bundle.getInt("sSoda")+", Water:"+bundle.getInt("water")+", Lassi:"+bundle.getInt("lassi")+", Vanilla:"+bundle.getInt("vanilla")+", Pista:"+bundle.getInt("pista")+", Stwbry:"+bundle.getInt("stwbry")+", Mango:"+bundle.getInt("mango")+", Btrsch:"+bundle.getInt("btrsch")+", kulfi:"+bundle.getInt("kulfi")+", Cbar:"+bundle.getInt("cbar")+", Fpack:"+bundle.getInt("fpack")+", Other:"+bundle.getInt("other")+", Other1:"+bundle.getInt("other1")+", Date:"+bundle.getString("datetime")));
                    tvTotal.setTooltipText(str);
                }
                tvTotal.setPadding(130,0,25,0);

                currDate1 = new TextView(getContext());
                currDate1.setText(String.valueOf(currDate));
                currDate1.setPadding(60,0,25,0);
                totalAll += Integer.parseInt(String.valueOf(res.getInt(27)));
                row.addView(srno1);
                row.addView(tvTotal);
                row.addView(currDate1);
                t.addView(row);
            }
            TableRow row = new TableRow(getContext());
            row.setHorizontalGravity(Gravity.START);
            TextView tvAll = new TextView(getContext());
            tvAll.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tvAll.setPadding(50,0,25,0);
            tvAll.setText(String.valueOf("Total: "+totalAll));
            row.addView(tvAll);
            t.addView(row);
        }else{
            TableRow rowEmpty = new TableRow(getContext());
            rowEmpty.setPadding(50,10,25,5);
            rowEmpty.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

            TextView tvEmpty = new TextView(getContext());
            tvEmpty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tvEmpty.setGravity(Gravity.CENTER_HORIZONTAL);

            tvEmpty.setText(String.valueOf("No data found"));
            rowEmpty.addView(tvEmpty);
            t.addView(rowEmpty);
        }
    }


    private void doPrintReport() throws FileNotFoundException {
        DbManager db = new DbManager(getContext());
        if (fromDate.getText().toString().equals("From date") || toDate.getText().toString().equals("To date")) {
            Toast.makeText(getContext(), "Please select Date", Toast.LENGTH_SHORT).show();
        } else {
            Cursor res = db.findRange(String.valueOf(fromDateFmt) , String.valueOf(toDateFmt));
            if (res.getCount() > 0) {
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
                System.out.println(path);
                Date date = new Date();
                String datePattern = "dd/MM/yyyy";
                String fileSaveDatePattern = "yyMMddhhmmss";
                SimpleDateFormat datFormatForFileSave = new SimpleDateFormat(fileSaveDatePattern);
                String fileExt = datFormatForFileSave.format(date.getTime());
                File file = new File(path, "report_" + fileExt + ".pdf");
                OutputStream outputStream = new FileOutputStream(file);

                PdfWriter pdfWriter = new PdfWriter(file);
                PdfDocument pdfDocument = new PdfDocument(pdfWriter);
                Document document = new Document(pdfDocument);
                pdfDocument.setDefaultPageSize(PageSize.A4.rotate());
                document.setMargins(20f, 10f, 10f, 10f);
//                document.setProperty(Property.LEADING, new Leading(Leading.MULTIPLIED, 1.2f));
                //image logic


                //

//                Paragraph paragraph = new Paragraph("Reports").setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER);

                Paragraph p2 = new Paragraph("Reports\nFrom date:" + fromDate.getText().toString() + " To date:" + toDate.getText().toString()).setBold().setFontSize(15).setTextAlignment(TextAlignment.CENTER);

                float[] columnWidth = {15f, 65f, 15f, 15f,15f,15f, 15f,15f,15f,15f, 15f, 15f, 15f, 15f,15f, 15f, 15f, 15f, 15f, 15f,15f, 15f, 15f, 15f, 15f, 15f, 20f, 40f};
                Table table = new Table(columnWidth);
                int n = 9;
                int total = 0, fpack=0,other = 0, other1 = 0,water = 0 , lassi = 0 ,cbar = 0,jsoda = 0, sSoda = 0 ,orange = 0, lemon = 0, kokam = 0, pachak = 0, ssrbt = 0, sarbat = 0, wala = 0, vanilla = 0, pista = 0, stwbry = 0, btrsch = 0, mango = 0, lsoda = 0, llemon = 0, lorange = 0,kulfi=0;
                table.setHorizontalAlignment(HorizontalAlignment.CENTER);
                table.setFontSize(n);
                int m = 8;
                table.setTextAlignment(TextAlignment.CENTER);
                table.setVerticalAlignment(VerticalAlignment.MIDDLE);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf("No")).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph("Name").setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(3))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(4))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(5))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(6))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(7))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(8))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(9))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(10))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(11))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(12))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(13))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(14))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(15))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(16))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(17))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(18))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(19))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(20))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(21))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(22))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(23))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(24))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(25))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(26))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf(res.getColumnName(27))).setBold())).setFontSize(m);
                table.addHeaderCell(new Cell().add(new Paragraph(String.valueOf("Date Time")).setBold())).setFontSize(m);

                while (res.moveToNext()) {
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(1)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getString(2)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(3)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(4)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(5)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(6)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(7)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(8)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(9)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(10)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(11)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(12)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(13)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(14)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(15)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(16)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(17)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(18)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(19)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(20)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(21)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(22)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(23)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(24)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(25)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(26)))));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getInt(27))).setBold()));
                    table.addCell(new Cell().add(new Paragraph(String.valueOf(res.getString(28)))));
                    orange = orange + res.getInt(3);
                    kokam = kokam + res.getInt(4);
                    lemon = lemon + res.getInt(5);
                    sarbat = sarbat + res.getInt(6);
                    pachak = pachak + res.getInt(7);
                    wala = wala + res.getInt(8);
                    lsoda = lsoda + res.getInt(9);
                    ssrbt = ssrbt + res.getInt(10);
                    lorange = lorange + res.getInt(11);
                    llemon = llemon + res.getInt(12);
                    jsoda = jsoda + res.getInt(13);
                    sSoda = sSoda + res.getInt(14);
                    water = water + res.getInt(15);
                    lassi = lassi + res.getInt(16);
                    vanilla = vanilla + res.getInt(17);
                    pista = pista + res.getInt(18);
                    stwbry = stwbry + res.getInt(19);
                    mango = mango + res.getInt(20);
                    btrsch = btrsch + res.getInt(21);
                    kulfi = kulfi + res.getInt(22);
                    cbar = cbar + res.getInt(23);
                    fpack = fpack + res.getInt(24);
                    other = other + res.getInt(25);
                    other1 = other1 + res.getInt(26);
                    total = total + res.getInt(27);
                }

                table.addCell(new Cell().add(new Paragraph(String.valueOf("Total")).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf("-"))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(orange)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(kokam)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(lemon)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(sarbat)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(pachak)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(wala)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(ssrbt)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(lsoda)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(lorange)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(llemon)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(jsoda)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(sSoda)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(water)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(lassi)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(vanilla)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(pista)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(stwbry)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(mango)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(btrsch)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(kulfi)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(cbar)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(fpack)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(other)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(other1)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(total)).setBold()));
                table.addCell(new Cell().add(new Paragraph(String.valueOf("-"))));

//        table.addCell(new Cell().add(new Paragraph("Name")));
//        table.addCell(new Cell().add(new Paragraph("Rohit")));
//
//        table.addCell(new Cell().add(new Paragraph("Town")));
//        table.addCell(new Cell().add(new Paragraph("Goa")));

//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//            table.addCell(new Cell().add(new Paragraph("Date")));
//            table.addCell(new Cell().add(new Paragraph(LocalDate.now().format(dateTimeFormatter))));
//
//
//        }

                Paragraph totalFinal = new Paragraph("\nRecord Found: " + res.getCount() + "\nTotal: " + total).setBold().setFontSize(12).setTextAlignment(TextAlignment.LEFT).setMarginLeft(30f);
//                document.add(paragraph);
                document.add(p2);
                document.add(table);
                document.add(totalFinal);
                document.close();
                Toast.makeText(getContext(), "Reports file generated successfully", Toast.LENGTH_SHORT).show();
                total = 0;
                orange = 0;
                lemon = 0;
                kokam = 0;
                pachak = 0;
                ssrbt = 0;
                sarbat = 0;
                wala = 0;
                jsoda = 0;
                sSoda = 0;
                water = 0;
                lassi = 0;
                vanilla = 0;
                pista = 0;
                stwbry = 0;
                btrsch = 0;
                mango = 0;
                kulfi = 0;
                lsoda = 0;
                llemon = 0;
                lorange = 0;
                other = 0;
                other1 = 0;
                fpack = 0;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("View");
                builder.setMessage("Do you want to view Pdf Document?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            File file1 = new File(path, "/report_" + fileExt + ".pdf");
                            Uri uri;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", file1);
                            } else {
                                uri = Uri.fromFile(file1);
                            }
                            intent.setDataAndType(uri, "application/pdf");
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(intent, "Open With .."));
                        } catch (Exception ex) {
                            Toast.makeText(getContext(), String.valueOf(ex), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                Toast.makeText(getContext(), "No Record Found", Toast.LENGTH_SHORT).show();
            }
        }

    }
}