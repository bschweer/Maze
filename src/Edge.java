// An edge links two neighboring points
class Edge
{
    private Point point;
    private int direction;
    private boolean isUsed;
    private boolean isDeleted;

    Edge(Point p, int d)
    {
        this.point = p;
        this.direction = d;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}