class Ball {
    private final int id;

    Ball(int id) {
        this.id = id;
    }

    int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Ball{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Ball ball = (Ball) o;

        return id == ball.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
