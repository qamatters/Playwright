const express = require('express');
const path = require('path');
const app = express();
const port = 3030;

// Serve static files from public folder
app.use(express.static(path.join(__dirname, 'public')));
app.use('/reports', express.static(path.join(__dirname, '..', 'reports')));


// Serve test-progress.json for live logs
app.get('/test-progress.json', (req, res) => {
    const filePath = path.join(__dirname, '..','reports', 'test-report', 'test-progress.json');
    res.sendFile(filePath, err => {
        if (err) {
            console.error("Error sending test-progress.json:", err);
            res.status(500).send("Error reading file.");
        }
    });
});

// Endpoint for history.json
app.get('/history', (req, res) => {
    const filePath = path.join(__dirname, '..', 'reports', 'test-report', 'history.json');
    res.sendFile(filePath, err => {
        if (err) {
            console.error("Error sending history.json:", err);
            res.status(500).send("Error reading file.");
        }
    });
});

// Endpoint for ExtentReport.html if needed
app.get('/extent-report', (req, res) => {
    const filePath = path.join(__dirname, '..', 'reports', 'ExtentReport.html');
    res.sendFile(filePath, err => {
        if (err) {
            console.error("Error sending ExtentReport.html:", err);
            res.status(500).send("Error reading file.");
        }
    });
});

// Redirect root to index.html
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'), err => {
        if (err) {
            console.error("Error sending index.html:", err);
            res.status(500).send("Error loading page.");
        }
    });
});

// Start the server
app.listen(port, () => {
    console.log(`Test Dashboard running at http://localhost:${port}`);
});
