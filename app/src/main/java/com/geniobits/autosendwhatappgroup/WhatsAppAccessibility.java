package com.geniobits.autosendwhatappgroup;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class WhatsAppAccessibility extends AccessibilityService {

        public static String TAG = WhatsAppAccessibility.class.getName();

        @Override
        public void onAccessibilityEvent (AccessibilityEvent event){

            //check if root window null or not
            if (getRootInActiveWindow() == null) {
                return;
            }
            PrefHelper prefHelper = new PrefHelper(getApplicationContext());
            if(!prefHelper.getOn()){
                return;
            }




            //getting root node
            AccessibilityNodeInfoCompat rootNodeInfo = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());

            List<AccessibilityNodeInfoCompat> sendMessageNodeList = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
            if (sendMessageNodeList != null && !sendMessageNodeList.isEmpty()) {
                AccessibilityNodeInfoCompat sendMessage = sendMessageNodeList.get(0);
                if (sendMessage.isVisibleToUser()) {
                    //"Send Button found click on it!"
                    sendMessage.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
                //fire send button
            } else {
                Log.e(TAG, "NO send button");
            }


            AccessibilityNodeInfo node = getRootInActiveWindow();
            if(node != null) {
                for(int i = 0; i < node.getChildCount(); i++){
                    AccessibilityNodeInfo childNode = node.getChild(i);
                    if(childNode != null){
                        Log.i("childNode", "-----getText->"+childNode.getText()+"---getContentDescription-->"+childNode.getContentDescription() );
                    }
                }
            }


//            AccessibilityNodeInfo source = findFocus(AccessibilityNodeInfo.FOCUS_INPUT);
//            String viewIdResourceName = source.getViewIdResourceName();
//            Log.e("sample king", viewIdResourceName);


            //get search icon in whats app

            List<AccessibilityNodeInfoCompat> searchNodeList = rootNodeInfo.findAccessibilityNodeInfosByViewId("//android.widget.ImageView[@bounds='[936,88][1080,256]']");

            if (searchNodeList != null && !searchNodeList.isEmpty()) {
                //if search button click on it
                Log.e(TAG, "searchNodeList not null");

                AccessibilityNodeInfoCompat searchIcn = searchNodeList.get(0);
                if (searchIcn.isVisibleToUser()){
                    //do nothing
                    searchIcn.performAction(AccessibilityNodeInfo.ACTION_CLICK);

                    //check if search text come up
                    List<AccessibilityNodeInfoCompat> searchTextNodeList = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/search_src_text");
                    if (searchTextNodeList != null && !searchTextNodeList.isEmpty()) {
                        //get search text
                        AccessibilityNodeInfoCompat searchText = searchTextNodeList.get(0);
                        if (searchText != null && StaticConfig.group_name!=null) {
                            // set text of search field
                            Bundle arguments = new Bundle();
                            arguments.putCharSequence(AccessibilityNodeInfo
                                    .ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, StaticConfig.group_name); //group name or contact name here
                            searchText.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);

                            //sleep until searching
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            //get contact list
                            List<AccessibilityNodeInfoCompat> contactPickerRowNode = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/contactpicker_row_name");
                            if (contactPickerRowNode != null && !contactPickerRowNode.isEmpty()) {
                                //open searched contact
                                AccessibilityNodeInfoCompat contactPickerRow = contactPickerRowNode.get(0);
                                if (contactPickerRow != null) {
                                    Log.e(TAG, "contactPickerRow" + contactPickerRow.getParent().isClickable());
                                    if(contactPickerRow.getParent().isClickable()) {
                                        //click on contact
                                        contactPickerRow.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);

                                        //click send button
                                        List<AccessibilityNodeInfoCompat> sendMessageNodeListSec = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
                                        if (sendMessageNodeListSec != null && !sendMessageNodeListSec.isEmpty()) {
                                            AccessibilityNodeInfoCompat sendMessageSec = sendMessageNodeListSec.get(0);
                                            if (sendMessageSec.isVisibleToUser()) {
                                                //fire send button
                                                sendMessageSec.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            }  //button not visible do nothing

                                        }  //do nothing

                                    }
                                } else {
                                    Log.e(TAG, "No contactPickerRow");
                                    performGlobalAction(GLOBAL_ACTION_BACK);
                                }
                            } else {
                                Log.e(TAG,"no row comes up");
                                performGlobalAction(GLOBAL_ACTION_BACK);
                                //do nothing if no row comes up
                            }
                        } else {
                            Log.e(TAG,"search text null");
                            performGlobalAction(GLOBAL_ACTION_BACK);
                            //do no thing if search text null
                        }
                    } else {
                        Log.e(TAG,"search text not present");
                        performGlobalAction(GLOBAL_ACTION_BACK);
                        //do nothing if search text not present
                    }
                }else {
                    Log.e(TAG,"no search icon");
                    performGlobalAction(GLOBAL_ACTION_BACK);
                    // if no search button do nothing
                }
            } else {
                Log.e(TAG,"no search button");
                // if no search button do nothing
            }



            try {
                Thread.sleep(2000); // some devices cant handle instant back click
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            performGlobalAction(GLOBAL_ACTION_BACK);

        }


        public static void logViewHierarchy (AccessibilityNodeInfo nodeInfo,final int depth){

            if (nodeInfo == null) return;

            String spacerString = "";

            for (int i = 0; i < depth; ++i) {
                spacerString += '-';
            }
            //Log the info you care about here... I choce classname and view resource name, because they are simple, but interesting.
            Log.d(TAG, spacerString + nodeInfo.getClassName() + " " + nodeInfo.getViewIdResourceName());

            for (int i = 0; i < nodeInfo.getChildCount(); ++i) {
                logViewHierarchy(nodeInfo.getChild(i), depth + 1);
            }
        }


        @Override
        public void onInterrupt () {

        }


}
