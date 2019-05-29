package com.krooms.hostel.rental.property.app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.krooms.hostel.rental.property.app.common.LogConfig;

import java.io.IOException;

public class DataBaseAdapter {
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public DataBaseAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DataBaseAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DataBaseAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            //LogConfig.logd(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public boolean savedRoomsInfo(String tblName, ContentValues cv) {

        try {

            Log.d("saved rooms info =", "informationsaved =" + mDb.insert("" + tblName, null, cv));
            return true;

        } catch (SQLException ex) {
            LogConfig.logd(TAG, "" + "Not saved Category =" + ex.getMessage());
            return false;
        }
    }

    public boolean insertPropertyImagesInfo(String tblName, ContentValues cv) {

        try {

            Log.d("saved image info =", "informationsaved =" + mDb.insert("" + tblName, null, cv));
            return true;

        } catch (SQLException ex) {
            LogConfig.logd(TAG, "" + "Not saved Category =" + ex.getMessage());
            return false;
        }
    }

    public boolean updatePropertyImagesInfo(String tblName, ContentValues cv, String property_id, String img_no) {

        try {

            Log.d("update rooms info =", "informationsaved ="
                    + mDb.update("" + tblName, cv, "property_id ='" + property_id + "' AND image_no ='" + img_no + "'", null));
            return true;

        } catch (SQLException ex) {
            LogConfig.logd(TAG, "" + "Not saved Category =" + ex.getMessage());
            return false;
        }
    }

    public boolean updateUserIdProofImagesInfo(String tblName, ContentValues cv, String rcu_id,String id_proof_type) {

        try {

            Log.d("update rooms info =", "informationsaved ="
                    + mDb.update("" + tblName, cv, "rcu_id ='" + rcu_id + "' AND id_proof_type ='" + id_proof_type + "'", null));
            return true;

        } catch (SQLException ex) {
            LogConfig.logd(TAG, "" + "Not saved Category =" + ex.getMessage());
            return false;
        }
    }

    public boolean updateRoomsInfo(String tblName, ContentValues cv, String property_id, String room_no) {

        try {

            Log.d("updateRoomsInfo =", "information update ="+room_no+" sqlquery = "
                    + mDb.update("" + tblName, cv, "property_id ='" + property_id + "' AND room_number ='" + room_no + "'", null));
            return true;

        } catch (SQLException ex) {
            LogConfig.logd(TAG, "" + "Not updateRoomsInfo =" + ex.getMessage());
            return false;
        }
    }

    public boolean updateRoomsInfo_With_RoomId(String tblName, ContentValues cv, String property_id, String room_id) {

        try {


            Log.d("updateRoomsInfoRoomId =", "information update ="+room_id+" sqlquery = "
                    + mDb.update("" + tblName, cv, "property_id ='" + property_id + "' AND room_id ='" + room_id + "'", null));

            return true;

        } catch (SQLException ex) {
            LogConfig.logd(TAG, "" + "Not update room =" + ex.getMessage());
            return false;
        }
    }

    public Cursor getCategory() {
        try {

            String sql = "SELECT * FROM category";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToLast();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            LogConfig.logd(TAG, "" + "get lb_category >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getCategoryId(String strId) {
        try {

            String sql = "SELECT * FROM category where cat_id = '" + strId + "'";
            LogConfig.logd(TAG, "" + "get Category id >>" + sql);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToLast();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            LogConfig.logd(TAG, "" + "get not category id >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public boolean deleteCategory() {
        try {

            String sql = "DELETE FROM category";
            mDb.execSQL(sql);
            LogConfig.logd(TAG, "" + "delete Category =" + sql);

            return true;
        } catch (SQLException mSQLException) {
            LogConfig.logd(TAG + "Not delete Category >>", "" + mSQLException.toString());
            return false;
        }
    }

    //////////////// Sub Category /////////////////////////////////////////////
    public boolean savedSubCategory(ContentValues cv) {

        try {
            mDb.insert("sub_category", null, cv);
            Log.d("saved Sub Category =", "informationsaved");
            return true;

        } catch (SQLException ex) {
            LogConfig.logd(TAG, "" + "Not saved sub_category =" + ex.getMessage());
            return false;
        }
    }

    public Cursor getSubCategory() {
        try {

            String sql = "SELECT * FROM sub_category";
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToLast();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            LogConfig.logd(TAG, "" + "get sub_category >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getSubCategoryId(String strId) {
        try {

            String sql = "SELECT * FROM sub_category where sub_cat_id = '" + strId + "'";
            LogConfig.logd(TAG, "" + "get sub_category id >>" + sql);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToLast();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            LogConfig.logd(TAG, "" + "get not sub_category id >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getSubCategoryByCategoryId(String strId) {
        try {

            String sql = "SELECT * FROM sub_category where cat_id = '" + strId + "'";
            LogConfig.logd(TAG, "" + "get sub_category id >>" + sql);
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToLast();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            LogConfig.logd(TAG, "" + "get not sub_category id >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public boolean deleteSubCategory() {
        try {

            String sql = "DELETE FROM sub_category";
            mDb.execSQL(sql);
            LogConfig.logd(TAG, "" + "delete sub_category =" + sql);

            return true;
        } catch (SQLException mSQLException) {
            LogConfig.logd(TAG + "Not delete sub_category >>", "" + mSQLException.toString());
            return false;
        }
    }

    //////////////// Country /////////////////////////////////////////////
//	public boolean savedCountry(ContentValues cv) {
//
//		try {
//			mDb.insert("country", null, cv);
//			Log.d("saved country =", "informationsaved");
//			return true;
//
//		} catch (SQLException ex) {
//			LogConfig.logd(TAG, "" + "Not saved country =" + ex.getMessage());
//			return false;
//		}
//	}
//
//	public Cursor getCountry() {
//		try {
//
//			String sql = "SELECT * FROM country";
//			Cursor mCur = mDb.rawQuery(sql, null);
//			if (mCur != null) {
//				mCur.moveToLast();
//			}
//			return mCur;
//		} catch (SQLException mSQLException) {
//			LogConfig.logd(TAG, "" + "get country >>" + mSQLException.toString());
//			throw mSQLException;
//		}
//	}
//	public Cursor getCountryId(String strId) {
//		try {
//
//			String sql = "SELECT * FROM country where country_id = '"+strId+"'";
//			LogConfig.logd(TAG, "" +"get country id >>" + sql);
//			Cursor mCur = mDb.rawQuery(sql, null);
//			if (mCur != null) {
//				mCur.moveToLast();
//			}
//			return mCur;
//		} catch (SQLException mSQLException) {
//			LogConfig.logd(TAG, "" + "get not country id >>" + mSQLException.toString());
//			throw mSQLException;
//		}
//	}
//
//	public boolean deleteCountry() {
//		try {
//
//			String sql = "DELETE FROM country";
//			mDb.execSQL(sql);
//			LogConfig.logd(TAG, "" + "delete country =" + sql);
//
//			return true;
//		} catch (SQLException mSQLException) {
//			LogConfig.logd(TAG + "Not delete country >>", "" + mSQLException.toString());
//			return false;
//		}
//	}
//
//	//////////////// States /////////////////////////////////////////////
//	public boolean savedState(ContentValues cv) {
//
//		try {
//			mDb.insert("state", null, cv);
//			Log.d("saved state =", "informationsaved");
//			return true;
//
//		} catch (SQLException ex) {
//			LogConfig.logd(TAG, "" + "Not saved state =" + ex.getMessage());
//			return false;
//		}
//	}
//
//	public Cursor getStates() {
//		try {
//
//			String sql = "SELECT * FROM state";
//			Cursor mCur = mDb.rawQuery(sql, null);
//			if (mCur != null) {
//				mCur.moveToLast();
//			}
//			return mCur;
//		} catch (SQLException mSQLException) {
//			LogConfig.logd(TAG, "" + "get state >>" + mSQLException.toString());
//			throw mSQLException;
//		}
//	}
//	public Cursor getStatesByCountryId(String strId) {
//		try {
//
//			String sql = "SELECT * FROM state where country_id = '"+strId+"'";
//			LogConfig.logd(TAG, "" +"get country id >>" + sql);
//			Cursor mCur = mDb.rawQuery(sql, null);
//			if (mCur != null) {
//				mCur.moveToLast();
//			}
//			return mCur;
//		} catch (SQLException mSQLException) {
//			LogConfig.logd(TAG, "" + "get not state id >>" + mSQLException.toString());
//			throw mSQLException;
//		}
//	}

    public Cursor getsqlRecords(String query) {
        try {

            //String sql = "SELECT * FROM state where state_id = '"+strId+"'";
            LogConfig.logd(TAG, "" + "get country id >>" + query);
            Cursor mCur = mDb.rawQuery(query, null);
            if (mCur != null) {
                mCur.moveToLast();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            LogConfig.logd(TAG, "" + "get not state id >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

//	public Cursor getStatesId(String strId) {
//		try {
//
//			String sql = "SELECT * FROM state where state_id = '"+strId+"'";
//			LogConfig.logd(TAG, "" +"get country id >>" + sql);
//			Cursor mCur = mDb.rawQuery(sql, null);
//			if (mCur != null) {
//				mCur.moveToLast();
//			}
//			return mCur;
//		} catch (SQLException mSQLException) {
//			LogConfig.logd(TAG, "" + "get not state id >>" + mSQLException.toString());
//			throw mSQLException;
//		}
//	}

    public Cursor getSqlqureies(String qurey) {


        try {

            LogConfig.logd(TAG, "" + " sql_qureies >>" + qurey);
            Cursor mCur = mDb.rawQuery(qurey, null);
            if (mCur != null) {
                mCur.moveToLast();
            }
            return mCur;
        } catch (SQLiteException mSQLException) {
            LogConfig.logd(TAG, "" + " not sql_qureies >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getSqlqureies_new(String qurey) {

        try {

            LogConfig.logd(TAG, "" + "get country id >>" + qurey);
            Cursor mCur = mDb.rawQuery(qurey, null);
            if (mCur != null) {
                mCur.moveToFirst();
            }
//            mDb.close();
            return mCur;
        } catch (SQLException mSQLException) {
            LogConfig.logd(TAG, "" + "get not state id >>" + mSQLException.toString());
            throw mSQLException;
        }


    }


    public Cursor getSqlqureiesIdProof(String qurey) {

        try {



            LogConfig.logd(TAG, "" + "get country id >>" + qurey);
            Cursor mCur = mDb.rawQuery(qurey, null);

            if (mCur != null ) {
                if  (mCur.moveToLast()) {
                    do {
                        String idProofType = mCur.getString(mCur.getColumnIndex("id_proof_type"));


                    }while (mCur.moveToPrevious());
                }
            }
            if (mCur != null) {
                mCur.moveToFirst();
            }
//            mDb.close();
            return mCur;
        } catch (SQLException mSQLException) {
            LogConfig.logd(TAG, "" + "get not state id >>" + mSQLException.toString());
            throw mSQLException;
        }


    }

    public boolean deleteStates() {
        try {

            String sql = "DELETE FROM state";
            mDb.execSQL(sql);
            LogConfig.logd(TAG, "" + "delete state =" + sql);

            return true;
        } catch (SQLException mSQLException) {
            LogConfig.logd(TAG + "Not delete state >>", "" + mSQLException.toString());
            return false;
        }
    }

    public boolean updateSynHistoryData(String tblName, ContentValues cv, String type) {

        try {

            Log.d("update sync_history =", "informationsaved ="
                    + mDb.update("" + tblName, cv, "sync_type ='" + type + "'", null));
            return true;

        } catch (SQLException ex) {
            LogConfig.logd(TAG, "" + "Not update sync_history =" + ex.getMessage());
            return false;
        }
    }

    public boolean savedSynHistoryData(ContentValues cv) {

        try {

            Log.d("saved sync_history =", "informationsaved"+mDb.insert("sync_history", null, cv));
            return true;

        } catch (SQLException ex) {
            LogConfig.logd(TAG, "" + "Not saved sync_history =" + ex.getMessage());
            return false;
        }
    }



}
