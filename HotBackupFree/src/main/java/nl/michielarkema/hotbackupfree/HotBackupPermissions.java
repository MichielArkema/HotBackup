package nl.michielarkema.hotbackupfree;

public enum HotBackupPermissions {
    USE {
        @Override
        public String toString() {
            return "hotbackup.use";
        }
    },
    START {
        @Override
        public String toString() {
            return "hotbackup.start";
        }
    },
    STATUS {
        @Override
        public String toString() {
            return "hotbackup.status";
        }
    },
    LIST {
        @Override
        public String toString() {
            return "hotbackup.list";
        }
    }
}
