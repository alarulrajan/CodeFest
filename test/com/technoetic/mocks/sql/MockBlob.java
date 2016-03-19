package com.technoetic.mocks.sql;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * The Class MockBlob.
 */
public class MockBlob implements Blob {
    
    /** The get binary stream return. */
    public InputStream getBinaryStreamReturn;
    
    /* (non-Javadoc)
     * @see java.sql.Blob#getBinaryStream()
     */
    public InputStream getBinaryStream() throws SQLException {
        return getBinaryStreamReturn;
    }

    /* (non-Javadoc)
     * @see java.sql.Blob#getBytes(long, int)
     */
    public byte[] getBytes(long pos, int length) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.sql.Blob#length()
     */
    public long length() throws SQLException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.sql.Blob#position(java.sql.Blob, long)
     */
    public long position(Blob pattern, long start) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.sql.Blob#position(byte[], long)
     */
    public long position(byte pattern[], long start) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.sql.Blob#setBinaryStream(long)
     */
    public OutputStream setBinaryStream(long pos) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.sql.Blob#setBytes(long, byte[])
     */
    public int setBytes(long pos, byte[] bytes) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.sql.Blob#setBytes(long, byte[], int, int)
     */
    public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.sql.Blob#truncate(long)
     */
    public void truncate(long len) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.sql.Blob#free()
     */
    public void free() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /* (non-Javadoc)
     * @see java.sql.Blob#getBinaryStream(long, long)
     */
    public InputStream getBinaryStream(long pos, long length) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
