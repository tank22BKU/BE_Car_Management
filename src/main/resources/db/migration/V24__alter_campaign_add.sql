-- Add version and new columns to campaign table (skip columns if already exist)
ALTER TABLE campaign 
    ADD COLUMN campaign_name VARCHAR(255),
    ADD COLUMN campaign_type VARCHAR(100),
    ADD COLUMN start_date DATE,
    ADD COLUMN end_date DATE,
    ADD COLUMN budget DECIMAL(18,2),
    ADD COLUMN target_audience VARCHAR(255),
    ADD COLUMN created_by VARCHAR(50),
    ADD COLUMN is_active BOOLEAN DEFAULT TRUE,
    ADD COLUMN social_media VARCHAR(255),
    ADD COLUMN search_engine VARCHAR(255),
    ADD COLUMN traditional_channel VARCHAR(255),
    ADD COLUMN campaign_goal VARCHAR(255),
    ADD COLUMN note_details TEXT,
    ADD COLUMN is_deleted BOOLEAN DEFAULT FALSE;

-- Drop unused columns
ALTER TABLE campaign
    DROP COLUMN content,
    DROP COLUMN is_read,
    DROP COLUMN created_at,
    DROP COLUMN updated_at;
