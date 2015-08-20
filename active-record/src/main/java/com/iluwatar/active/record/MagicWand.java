package com.iluwatar.active.record;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * "Every single wand is unique and will depend for its character on the particular tree and magical creature
 * from which it derives its materials. Moreover, each wand, from the moment it finds its ideal owner, will begin
 * to learn from and teach its human partner." @ Garrick Ollivander
 * <p/>
 * Created by Stephen Lazarionok.
 */
public class MagicWand {

    private Long id;

    private double lengthInches;

    private WandWoodType wood;

    private WandCoreType core;

    public Long getId() {
        return id;
    }

    public double getLengthInches() {
        return lengthInches;
    }

    public WandWoodType getWood() {
        return wood;
    }

    public WandCoreType getCore() {
        return core;
    }

    public void setLengthInches(double lengthInches) {
        this.lengthInches = lengthInches;
    }

    public void setWood(WandWoodType wood) {
        this.wood = wood;
    }

    public void setCore(WandCoreType core) {
        this.core = core;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MagicWand(");
        sb.append(getWood());
        sb.append(", ");
        sb.append(getCore());
        sb.append(", ");
        sb.append(getLengthInches());
        sb.append(" inch(es))");
        return sb.toString();
    }

    /**
     * ***********************************
     * ****** Data access methods
     * ***********************************
     */

    private static final String SELECT_SQL = "select * from wand where id = ?";
    private static final String DELETE_SQL = "delete from wand where id = ?";
    private static final String UPDATE_SQL = "update wand set length_inches = ?, wood = ?, core = ? where id = ?";
    private static final String CREATE_SQL = "insert into wand values(?, ?, ?, ?)";

    /**
     * Saves the instance to the DB.
     * @return
     */
    public long save() {
        validateToSave();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DB.getConnection();
            ps = connection.prepareStatement(CREATE_SQL);

            final long id = System.currentTimeMillis();
            ps.setLong(1, id);
            ps.setDouble(2, getLengthInches());
            ps.setString(3, getWood().name());
            ps.setString(4, getCore().name());

            ps.execute();


            return id;
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closePreparedStatement(ps);
            DB.closeConnection(connection);
        }
    }

    /**
     * Deletes the instance from the DB.
     */
    public void delete() {
        validateToDelete();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DB.getConnection();
            ps = connection.prepareStatement(DELETE_SQL);


            ps.setLong(1, getId());
            ps.execute();
            ps.close();
            connection.close();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closePreparedStatement(ps);
            DB.closeConnection(connection);
        }
    }

    /**
     * Updates the instance in DB.
     */
    public void update() {
        validateToUpdate();
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DB.getConnection();
            ps = connection.prepareStatement(UPDATE_SQL);


            ps.setDouble(1, getLengthInches());
            ps.setString(2, getWood().name());
            ps.setString(3, getCore().name());
            ps.setLong(4, getId());

            ps.execute();
            ps.close();
            connection.close();
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closePreparedStatement(ps);
            DB.closeConnection(connection);
        }
    }

    /**
     * Finds the instance in the DB.
     * @param id
     * @return
     */
    public static MagicWand find(long id) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = DB.getConnection();
            ps = connection.prepareStatement(SELECT_SQL);

            ResultSet rs;
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                final MagicWand wand = new MagicWand();

                wand.id = rs.getLong("id");
                wand.setCore(WandCoreType.valueOf(rs.getString("core")));
                wand.setWood(WandWoodType.valueOf(rs.getString("wood")));
                wand.setLengthInches(rs.getDouble("length_inches"));

                return wand;
            }
            else {
                return null;
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DB.closePreparedStatement(ps);
            DB.closeConnection(connection);
        }

    }

    private void validateToSave() {
        validateProperties();
        if (getId() != null)
            throw new IllegalStateException("Can not save wand that was previously saved. Use 'update' metod instead.");
    }

    private void validateToUpdate() {
        validateProperties();
        if (getId() == null) throw new IllegalStateException("Can not update a record without ID specified");
    }

    private void validateToDelete() {
        if (getId() == null) throw new IllegalStateException("Can not delete a record without ID specified");
    }

    private void validateProperties() {
        if (getLengthInches() > 0.0d)
            throw new IllegalStateException("Can not save a wand with length <= 0");
        if (getWood() == null)
            throw new IllegalStateException("Can not save a wand without wood specified");
        if (getCore() == null)
            throw new IllegalStateException("Can not save a wand without core specified");
    }

    /**
     * ***********************************
     * ****** Domain Logic
     * ***********************************
     */

    private double calculateWoodMagicFactor() {

        switch (getWood()) {
            case HOLLY:
                return 1.2d;
            case WINE:
                return 1.0d;
            case HAWTHORN:
                return 0.8d;
            default:
                return 0.0d;
        }
    }

    private double calculateCoreMagicFactor() {

        switch (getCore()) {
            case PHOENIX_FEATHER:
                return 1.2d;
            case DRAGON_HEARTSTRING:
                return 1.0d;
            case UNICORN_HAIR:
                return 0.8d;
            default:
                return 0.0d;
        }
    }

    public double calculateMagicPower() {

        return calculateWoodMagicFactor() * calculateWoodMagicFactor() * getLengthInches();
    }

    public void castFireball() throws SpellCastException {

        if (calculateMagicPower() >= 10.0) {
            System.out.println(toString() + ": a fireball spell is casted");
        } else {
            throw new SpellCastException("Can not cast fire ball! Not enough magic power...");
        }
    }

    public void castLighting() throws SpellCastException {
        if (calculateMagicPower() >= 20.0) {
            System.out.println(toString() + ": a lighting spell is casted");
        } else {
            throw new SpellCastException("Can not cast lighting! Not enough magic power...");
        }
    }

}
