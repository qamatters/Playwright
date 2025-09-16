let progressChart;
let historyChart;

const logsContainer = document.getElementById('liveLogs');

async function fetchData() {
    try {
        const res = await fetch('/test-progress.json?' + new Date().getTime());
        const data = await res.json();

        const total = data.totalTests || 0;
        const completed = data.testsCompleted || 0;
        const passed = data.tests.filter(t => t.status === 'Passed').length;
        const failed = data.tests.filter(t => t.status === 'Failed').length;

        document.getElementById('totalTests').innerText = total;
        document.getElementById('testsCompleted').innerText = completed;
        document.getElementById('testsPassed').innerText = passed;
        document.getElementById('testsFailed').innerText = failed;

        // Test table
        const table = document.getElementById('testTable');
        table.innerHTML = '';
        data.tests.forEach(test => {
            const row = document.createElement('tr');
            row.className = 'border-b border-gray-700 hover:bg-gray-700';

            let statusColor = 'text-gray-300';
            if (test.status === 'Passed') statusColor = 'text-green-400 font-semibold';
            else if (test.status === 'Failed') statusColor = 'text-red-400 font-semibold';
            else if (test.status === 'Running') statusColor = 'text-yellow-400 font-semibold';

            const failureReason = test.status === 'Failed' ? (test.logs.join(' | ') || 'Unknown') : '-';

            row.innerHTML = `
                <td class="px-4 py-2">${test.testName}</td>
                <td class="px-4 py-2 ${statusColor}">${test.status}</td>
                <td class="px-4 py-2">${test.startTime || '-'}</td>
                <td class="px-4 py-2">${test.endTime || '-'}</td>
                <td class="px-4 py-2">${test.durationSec !== undefined ? test.durationSec + " sec" : "-"}</td>
                <td class="px-4 py-2">${failureReason}</td>
            `;
            table.appendChild(row);
        });

        // Progress chart
        const notRun = total - passed - failed;
        const chartData = {
            labels: ['Passed', 'Failed', 'Not Run'],
            datasets: [{ data: [passed, failed, notRun], backgroundColor: ['#16a34a', '#dc2626', '#9ca3af'] }]
        };

        if (!progressChart) {
            const ctx = document.getElementById('progressChart').getContext('2d');
            progressChart = new Chart(ctx, { type: 'doughnut', data: chartData, options: { responsive: true, plugins: { legend: { position: 'bottom' } } } });
        } else {
            progressChart.data = chartData;
            progressChart.update();
        }

        // Historical chart
        const historyRes = await fetch('/history');
        const historyData = await historyRes.json();
        const labels = historyData.map(r => r.runId);
        const passPercent = historyData.map(r => r.passPercent);

        if (!historyChart) {
            const ctx2 = document.getElementById('historyChart').getContext('2d');
            historyChart = new Chart(ctx2, {
                type: 'line',
                data: { labels, datasets: [{ label: 'Pass %', data: passPercent, borderColor: '#3b82f6', backgroundColor: 'rgba(59,130,246,0.2)', fill: true, tension: 0.3 }] },
                options: { responsive: true, plugins: { legend: { display: true } }, scales: { y: { min: 0, max: 100, ticks: { stepSize: 10 } } } }
            });
        } else {
            historyChart.data.labels = labels;
            historyChart.data.datasets[0].data = passPercent;
            historyChart.update();
        }

    } catch (error) {
        console.error("Error fetching data:", error);
    }
}

async function fetchLogs() {
    try {
        const res = await fetch('/test-progress.json?' + new Date().getTime());
        const data = await res.json();
        logsContainer.innerHTML = '';

        const isRunning = data.tests.some(t => t.status === 'Running');

        if (isRunning) {
            data.tests.forEach(test => {
                test.logs.forEach(line => {
                    const text = line.message || "Unknown";
                    const div = document.createElement('div');

                    if (text.includes('Passed')) div.className = 'text-green-400 font-semibold';
                    else if (text.includes('Failed')) div.className = 'text-red-400 font-semibold';
                    else if (text.includes('Warning')) div.className = 'text-yellow-400';
                    else div.className = 'text-gray-300';

                    const timestamp = line.timestamp || new Date().toLocaleTimeString();
                    div.innerText = `[${timestamp}] [${test.testName}] ${text}`;
                    logsContainer.appendChild(div);
                });
            });
            logsContainer.scrollTop = logsContainer.scrollHeight;
        } else {
            // Show friendly message + Extent Report link at top-right
            const container = document.createElement('div');
            container.className = 'w-full flex justify-between items-center mt-2';

            const message = document.createElement('span');
            message.className = 'text-gray-500 italic';
            message.innerText = 'No test cases are running right now. Live feed will appear once execution starts.';

            const reportLink = document.createElement('a');
            reportLink.href = '/extent-report';
            reportLink.target = '_blank';
            reportLink.className = 'text-blue-500 underline font-semibold';
            reportLink.innerText = 'View Last Execution report';

            container.appendChild(message);
            container.appendChild(reportLink);

            logsContainer.appendChild(container);
        }

    } catch (err) {
        logsContainer.innerHTML = `<div class="text-red-500 mt-2">Enable Extent logging to see live logs</div>`;
        console.error(err);
    }
}


// Polling intervals
setInterval(fetchData, 2000);
setInterval(fetchLogs, 1000);
fetchData();
fetchLogs();
