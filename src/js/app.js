document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('loginForm')?.addEventListener('submit', function(e) {
        e.preventDefault();
        window.location.href = 'src/pages/index.html';
    });
});

function showCreateModal() {
    document.getElementById('createModal')?.classList.add('show');
}

function hideCreateModal() {
    document.getElementById('createModal')?.classList.remove('show');
}

function showCreateApproval() {
    document.getElementById('createApprovalModal')?.classList.add('show');
}

function hideCreateApproval() {
    document.getElementById('createApprovalModal')?.classList.remove('show');
}

function showUploadModal() {
    document.getElementById('uploadModal')?.classList.add('show');
}

function hideUploadModal() {
    document.getElementById('uploadModal')?.classList.remove('show');
}

function showCreateMeeting() {
    document.getElementById('createMeetingModal')?.classList.add('show');
}

function hideCreateMeeting() {
    document.getElementById('createMeetingModal')?.classList.remove('show');
}

function showCreateUser() {
    document.getElementById('createUserModal')?.classList.add('show');
}

function hideCreateUser() {
    document.getElementById('createUserModal')?.classList.remove('show');
}

function showDetail() {
    alert('查看详情功能开发中...');
}
