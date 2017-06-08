package com.zano.shareride.adapters;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

/**
 * Created by Zano on 08/06/2017, 11:50.
 */

public abstract class FirebaseRecyclerAdapter<FMVH extends BaseRecyclerAdapter.ViewHolder,FD> extends BaseRecyclerAdapter<FMVH,FD> implements ChildEventListener {

    private DatabaseReference reference;
    private Class<FD> clazz;

    public FirebaseRecyclerAdapter(Context context, int listItemLayoutId, Map data, String referencePath, Class<FD> clazz) {
        super(context, listItemLayoutId, data);
        this.clazz = clazz;
        this.reference = FirebaseDatabase.getInstance().getReference(referencePath);
        this.reference.addChildEventListener(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        FD value = dataSnapshot.getValue(clazz);
        String key = dataSnapshot.getKey();
        data.put(key,value);
        updatedData(value);
        notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        FD value = dataSnapshot.getValue(clazz);
        String key = dataSnapshot.getKey();
        data.put(key,value);
        updatedData(value);
        notifyDataSetChanged();
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        data.remove(key);
        updatedData(null);
        notifyDataSetChanged();
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, "onChildMoved:no operation implemented");
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d(TAG, "onCancelled:" + databaseError.getMessage());
    }

    /**
     * @param value notify that the data is changed. Null if I have removed a value. I also pass as parameter the value changed
     */
    protected abstract void updatedData(FD value);
}
